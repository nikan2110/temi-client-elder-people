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
    String userName = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");


        if (title.equals("physical_activity")) {
            robot.addTtsListener(this);
            Log.i("titleActivity", "start physical activity");
            robot.speak(TtsRequest.create("בוקר טוב, נתחיל את הבוקר בכיף עם פעילות גופנית"));
        }

        if (title.equals("news_activity")) {
            robot.addTtsListener(this);
            Log.i("titleActivity", "start news activity");
            robot.speak(TtsRequest.create("עכשיו הגיע הזמן לקצת חדשות"));
        }

        if (title.equals("breakfast_activity")) {
            Log.i("titleActivity", "start breakfast activity");
            robot.speak(TtsRequest.create("בוקר טוב, " + userName +  "." +
                    " ארוחת הבוקר שלך מחכה לך, בתאבון. לא לשכוח לשתות לפחות כוס מים אחת"));
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
            robot.speak(TtsRequest.create(userName + ", הנה תזכורת שיש אצלי, היום יש יום הולדת לנכדה, בוא נתקשר אליה"));
        }

    }

    private void startVideoActivity(Context context) {
        Intent videoIntent = new Intent(context, VideoActivity.class);
        videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Необходимо для запуска Activity из BroadcastReceiver
        context.startActivity(videoIntent);
    }

    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        Log.i("ttsRequest", ttsRequest.getStatus().toString());
        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            if (ttsRequest.getSpeech().equals("בוקר טוב, נתחיל את הבוקר בכיף עם פעילות גופנית")) {
                robot.removeTtsListener(this);
                startVideoActivity(context);
            }
            if (ttsRequest.getSpeech().equals("עכשיו הגיע הזמן לקצת חדשות")) {
                robot.removeTtsListener(this);
                newsService.getTopTwoNewsByTheme("Israel");
            }
            if (ttsRequest.getSpeech().equals(userName + ", הנה תזכורת שיש אצלי, היום יש יום הולדת לנכדה, בוא נתקשר אליה")) {
                robot.removeTtsListener(this);
                Log.i("contacts", robot.getAllContact().toString());
                robot.startTelepresence("nikita", "1b1b95112a0b58a28e13aefb1340fcb8");
            }
        }
    }
}
