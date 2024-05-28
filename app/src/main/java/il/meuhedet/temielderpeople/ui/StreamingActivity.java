package il.meuhedet.temielderpeople.ui;

import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.utils.CameraService;

public class StreamingActivity extends AppCompatActivity {

    CameraService cameraService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        cameraService = new CameraService(this);

        startButton.setOnClickListener(v -> {
            cameraService.openCamera();
        });

        stopButton.setOnClickListener(v -> {
            cameraService.closeCamera();
        });


    }
}