package il.meuhedet.temielderpeople.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import il.meuhedet.temielderpeople.R;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView videoView = findViewById(R.id.videoView);
        Button closeButton = findViewById(R.id.closeButton);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.elder_people_activity;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();

        closeButton.setOnClickListener(view -> {
            videoView.stopPlayback();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoView videoView = findViewById(R.id.videoView);
        videoView.stopPlayback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoView videoView = findViewById(R.id.videoView);
        videoView.stopPlayback();
    }

}