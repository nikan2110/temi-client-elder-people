package il.meuhedet.temielderpeople.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String userName = intent.getStringExtra("USER_NAME");
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");

        Robot robot = Robot.getInstance();

        robot.speak(TtsRequest.create("Hello, " + userName +  "." + " This is a notification " +
                "about your activity. " + title + "." + description));

    }
}
