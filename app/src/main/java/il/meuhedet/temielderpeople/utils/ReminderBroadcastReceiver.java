package il.meuhedet.temielderpeople.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import il.meuhedet.temielderpeople.api.services.NewsService;
import il.meuhedet.temielderpeople.ui.MedicationListActivity;
import il.meuhedet.temielderpeople.ui.VideoActivity;

public class ReminderBroadcastReceiver extends BroadcastReceiver implements Robot.TtsListener {

    NewsService newsService = new NewsService();
    Context context = null;
    Robot robot = Robot.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");


        if (title.equals("physical_activity")) {
            robot.addTtsListener(this);
            Log.i("titleActivity", "start physical activity");
            robot.speak(TtsRequest.create("Time to start physical activity"));
        }

        if (title.equals("news_activity")) {
            robot.addTtsListener(this);
            Log.i("titleActivity", "start news activity");
            robot.speak(TtsRequest.create("Here some news about Israel"));
        }

        if (title.equals("breakfast_activity")) {
            Log.i("titleActivity", "start breakfast activity");
            robot.speak(TtsRequest.create("Hello, " + userName +  "." +
                    " Time to eat your breakfast"));
        }

        if (title.equals("drugs_activity")) {
            Log.i("titleActivity", "start drugs activity");
            Intent medicalListIntent = new Intent(context, MedicationListActivity.class);
            medicalListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(medicalListIntent);
        }

        if (title.equals("call_activity")) {
            robot.addTtsListener(this);
            Log.i("titleActivity", "start call activity");
            robot.speak(TtsRequest.create("Reminder, today is happy birthday " +
                    "of your granddaughter. Let's call her"));
        }

    }

    private void startVideoActivity(Context context) {
        Intent videoIntent = new Intent(context, VideoActivity.class);
        videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Необходимо для запуска Activity из BroadcastReceiver
        context.startActivity(videoIntent);
    }

    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            if (ttsRequest.getSpeech().equals("Time to start physical activity")) {
                robot.removeTtsListener(this);
                startVideoActivity(context);
            }
            if (ttsRequest.getSpeech().equals("Here some news about Israel")) {
                robot.removeTtsListener(this);
                newsService.getTopTwoNewsByTheme("Israel");
            }
            if (ttsRequest.getSpeech().equals("Reminder, today is happy birthday " +
                    "of your granddaughter. Let's call her")) {
                robot.removeTtsListener(this);
                Log.i("contacts", robot.getAllContact().toString());
                robot.startTelepresence("nikita", "1b1b95112a0b58a28e13aefb1340fcb8");
            }
        }
    }
}
