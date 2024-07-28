package il.meuhedet.temielderpeople.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.widget.Toast;

//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.utils.ReminderBroadcastReceiver;

public class MedicationListActivity extends AppCompatActivity {

    private ArrayList<String> medications;
    private ArrayAdapter<String> adapter;
//    private Robot robot = Robot.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        setTitle("Medications to Take");

        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> finish());

        medications = new ArrayList<>(Arrays.asList("אספירין", "איבורפן", "אקמול"));

        ListView listView = findViewById(R.id.listViewMedications);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medications);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            askMedicationConfirmation(medications.get(position));
        });

        Toast.makeText(this, "Please check your medication " +
                "list and confirm each as you take them.", Toast.LENGTH_LONG).show();
    }


    private void askMedicationConfirmation(String medication) {
//        robot.speak(TtsRequest.create("האם לקחת את ה" + medication + " ?"));
        Toast.makeText(this, "האם לקחת את ה" + medication + " ?", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Medication");
        builder.setMessage("האם לקחת את ה" + medication + "?");

        builder.setPositiveButton("כן לקחתי", (dialog, which) -> {
            medications.remove(medication);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "מצויין, עדכנתי את האחות שלקחת את התרופה "
                    + medication + ".", Toast.LENGTH_LONG).show();
//            robot.speak(TtsRequest.create("מצויין, עדכנתי את האחות שלקחת את התרופה " + medication + "."));
        });

        builder.setNegativeButton("לא", (dialog, which) -> {
            Toast.makeText(this, "אני אזכיר לך שוב בעוד חצי שעה לקחת " +
                    medication + ".", Toast.LENGTH_LONG).show();
            setReminderAlarm(this, medication);
//            robot.speak(TtsRequest.create("אני אזכיר לך שוב בעוד חצי שעה לקחת " + medication + "."));
        });

        builder.create().show();
    }

    public void setReminderAlarm(Context context, String medication) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("TITLE", "remind_drug");
        intent.putExtra("MEDICATION", medication);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        long alarmTime = calendar.getTimeInMillis();

        Log.i("AlarmSet", "Setting alarm for ID: " + 0 + " at "
                + calendar.getTime());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
    }
}