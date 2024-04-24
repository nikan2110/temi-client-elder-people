package il.meuhedet.temielderpeople.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.services.NewsService;
import il.meuhedet.temielderpeople.ui.VideoActivity;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    NewsService newsService = new NewsService();


    @Override
    public void onReceive(Context context, Intent intent) {
        String userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");

        if (title.equals("physical_activity")) {
            Log.i("titleActivity", "video start");
            Intent videoIntent = new Intent(context, VideoActivity.class);
            videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Необходимо для запуска Activity из BroadcastReceiver
            context.startActivity(videoIntent);
        }
        if (title.equals("breakfast_activity")) {
            Log.i("breakfast_activity", description);
        }
        if (title.equals("news_activity")) {
            newsService.getTopFiveNewsByTheme("Israel");
        }

    }
}
