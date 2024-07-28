package il.meuhedet.temielderpeople.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.TtsRequest;
import il.meuhedet.temielderpeople.api.services.NewsService;
import il.meuhedet.temielderpeople.ui.MedicationListActivity;
import il.meuhedet.temielderpeople.ui.VideoActivity;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    Context context = null;
    String userName = "";
    TtsManager ttsManager = TtsManager.getInstance(context);

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");


        if (title.equals("physical_activity")) {
            Log.i("titleActivity", "start physical activity");
            Toast.makeText(context, "בוקר טוב, נתחיל את הבוקר בכיף עם פעילות גופנית",
                    Toast.LENGTH_LONG).show();
//            ttsManager.speak(TtsRequest.create("בוקר טוב, נתחיל את הבוקר בכיף עם פעילות גופנית",
//                            true),
//                    false);
        }

        if (title.equals("news_activity")) {
            Log.i("titleActivity", "start news activity");
            Toast.makeText(context, "עכשיו הגיע הזמן לקצת חדשות",
                    Toast.LENGTH_LONG).show();
//            ttsManager.speak(TtsRequest.create("עכשיו הגיע הזמן לקצת חדשות",
//                            true),
//                    false);
        }

        if (title.equals("breakfast_activity")) {
            Log.i("titleActivity", "start breakfast activity");
            Toast.makeText(context, "בוקר טוב, " + userName +  "." +
                            " ארוחת הבוקר שלך מחכה לך, בתאבון. לא לשכוח לשתות לפחות כוס מים אחת",
                    Toast.LENGTH_LONG).show();
//            ttsManager.speak(TtsRequest.create("בוקר טוב, " + userName +  "." +
//                    " ארוחת הבוקר שלך מחכה לך, בתאבון. לא לשכוח לשתות לפחות כוס מים אחת",
//                            true),
//                    false);
        }

        if (title.equals("drugs_activity")) {
            Log.i("titleActivity", "start drugs activity");
            Intent medicalListIntent = new Intent(context, MedicationListActivity.class);
            medicalListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(medicalListIntent);
        }

        if (title.equals("call_activity")) {
            Log.i("titleActivity", "start call activity");
            Toast.makeText(context, "הנה תזכורת שיש אצלי, היום יש יום" +
                    " הולדת לנכדה, בוא נתקשר אליה",
                    Toast.LENGTH_LONG).show();
//            ttsManager.speak(TtsRequest.create(", הנה תזכורת שיש אצלי, היום יש יום" +
//                            " הולדת לנכדה, בוא נתקשר אליה"),
//                    false);
        }
    }
}
