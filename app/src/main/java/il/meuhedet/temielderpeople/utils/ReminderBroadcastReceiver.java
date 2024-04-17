package il.meuhedet.temielderpeople.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Long id = intent.getLongExtra("ID", 1);
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");

        Log.i("message", id + " " +  title + " " + description);

    }
}
