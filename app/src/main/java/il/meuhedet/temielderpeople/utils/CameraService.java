package il.meuhedet.temielderpeople.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.*;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraService {
    private static final String TAG = "CameraService";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Context context;
    private CameraDevice cameraDevice;
    private MediaCodec mediaCodec;
    private Surface inputSurface;
    private Handler backgroundHandler;
    private CameraCaptureSession captureSession;
    private Socket socket;
    private String serverAddress = "192.168.12.110"; // IP адрес сервера
    private int serverPort = 5999; // Порт сервера

    private volatile boolean encodingActive;  // Флаг активности потока кодирования

    public CameraService(Context context) {
        this.context = context;
        initializeBackgroundHandler();
    }

    private void initializeBackgroundHandler() {
        HandlerThread handlerThread = new HandlerThread("CameraBackground");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    public void openCamera() {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    cameraDevice = camera;
                    setupMediaCodec();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                    cameraDevice = null;
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Camera access exception: ", e);
        }
    }

    private void setupMediaCodec() {
        try {
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
                    1920, 1080);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 5000000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            inputSurface = mediaCodec.createInputSurface();
            mediaCodec.start();

            startCameraCaptureSession();


        } catch (IOException e) {
            Log.e(TAG, "MediaCodec configuration exception: ", e);
        }
    }

    private void startCameraCaptureSession() {
        try {
            CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            captureBuilder.addTarget(inputSurface);  // Вывод на surface, которую создал MediaCodec

            cameraDevice.createCaptureSession(Arrays.asList(inputSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        captureSession.setRepeatingRequest(captureBuilder.build(), null,
                                backgroundHandler);
                        startEncodingThread();
                    } catch (CameraAccessException e) {
                        Log.e(TAG, "Failed to start camera preview because: ", e);
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e(TAG, "Failed to configure camera");
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Camera access exception while setting up preview", e);
        }

    }

    private void startEncodingThread() {
        encodingActive = true;
        Thread encodingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                handleEncodedVideo();
            }
        });
        encodingThread.start();
    }

    public void closeCamera() {
        encodingActive = false;

        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close socket: ", e);
            }
        }
    }

    private void handleEncodedVideo() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        ByteBuffer outputBuffer;
        int outputBufferIndex;
        byte[] videoChunk;

        while (encodingActive) {

            if (mediaCodec == null) {
                break;
            }

            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            if (outputBufferIndex >= 0) {
                outputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);
                if (bufferInfo.size > 0) {
                    videoChunk = new byte[bufferInfo.size];
                    outputBuffer.get(videoChunk);
                    sendDataToServer(videoChunk);
                }
                outputBuffer.clear();
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // Попробуйте позже
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // Формат выходных данных изменился, обработайте, если это необходимо
            }
        }
    }

    private void sendDataToServer(byte[] data) {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(serverAddress, serverPort);
            }
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            Log.e(TAG, "Error sending data: " + e.getMessage());
            try {
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException ex) {
                Log.e(TAG, "Error closing socket: " + ex.getMessage());
            }
        }
    }
}
