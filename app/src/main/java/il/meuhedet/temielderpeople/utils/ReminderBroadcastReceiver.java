package il.meuhedet.temielderpeople.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");

    }
}
