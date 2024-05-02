package il.meuhedet.temielderpeople.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.controllers.ActivityController;
import il.meuhedet.temielderpeople.api.controllers.UserController;
import il.meuhedet.temielderpeople.api.dto.ActivityDTO;
import il.meuhedet.temielderpeople.api.dto.UserRegistrationDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import il.meuhedet.temielderpeople.utils.ReminderBroadcastReceiver;
import il.meuhedet.temielderpeople.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RemindersActivity extends AppCompatActivity {

    private LinearLayout activitiesContainer;
    Retrofit retrofit = RetrofitClientTemiServer.getClient();
    ActivityController activityController = retrofit.create(ActivityController.class);
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        activitiesContainer = findViewById(R.id.activitiesContainer);

        Button buttonAddActivity = findViewById(R.id.buttonAddActivity);
        Button buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        buttonAddActivity.setOnClickListener(v -> {
            startActivity(new Intent(RemindersActivity.this, AddActivityActivity.class));
        });

        buttonBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Очищаем стек активностей
            startActivity(intent);
        });

        String userId = sharedPreferencesManager.getUserId();
        loadActivities(userId);

    }

    private void loadActivities(String userId) {
        activityController.getAllActivitiesByUserId(userId).enqueue(new Callback<List<ActivityDTO>>() {

            @Override
            public void onResponse(Call<List<ActivityDTO>> call, Response<List<ActivityDTO>> response) {
                if (response.code() == 200) {
                    List<ActivityDTO> activities = response.body();
                    if (activities != null && !activities.isEmpty()) {
                        displayActivities(activities);
                        for (ActivityDTO activity : activities) {
                            setReminder(RemindersActivity.this, activity);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ActivityDTO>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }

    private void setReminder(Context context, ActivityDTO activity) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                scheduleAlarm(alarmManager, context, activity);
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
            }
        } else {
            scheduleAlarm(alarmManager, context, activity);
        }
    }

    private void scheduleAlarm(AlarmManager alarmManager, Context context, ActivityDTO activity) {
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("USER_NAME", sharedPreferencesManager.getUserName());
        intent.putExtra("TITLE", activity.getTitle());
        intent.putExtra("DESCRIPTION", activity.getDescription());

        // Перебор всех дней недели из DTO и установка отдельного будильника на каждый
        for (DayOfWeek day : activity.getDays()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    activity.getId().intValue() * 100 + day.getValue(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);



            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, LocalTime.parse(activity.getTime()).getHour());
            calendar.set(Calendar.MINUTE, LocalTime.parse(activity.getTime()).getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, convertDayOfWeek(day));

            // Если время уже прошло на этой неделе, планируем на следующую
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            Log.i("AlarmSet", "Setting alarm for ID: "
                    + activity.getId().intValue() * 100 + day.getValue() + " at " + calendar.getTime());

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private int convertDayOfWeek(DayOfWeek day) {
        switch (day) {
            case TUESDAY: return Calendar.TUESDAY;
            case WEDNESDAY: return Calendar.WEDNESDAY;
            case THURSDAY: return Calendar.THURSDAY;
            case FRIDAY: return Calendar.FRIDAY;
            case SATURDAY: return Calendar.SATURDAY;
            case SUNDAY: return Calendar.SUNDAY;
            default: return Calendar.MONDAY;
        }
    }



    private void displayActivities(List<ActivityDTO> activities) {
        for (ActivityDTO activity : activities) {
            View activityView = LayoutInflater.from(this).inflate(R.layout.activity_item, activitiesContainer, false);
            populateActivityView(activityView, activity);
            activitiesContainer.addView(activityView);
        }
    }

    private void populateActivityView(View view, ActivityDTO activity) {
        ((TextView) view.findViewById(R.id.textViewTitle)).setText(activity.getTitle());
        ((TextView) view.findViewById(R.id.textViewDescription)).setText(activity.getDescription());
        ((TextView) view.findViewById(R.id.textViewTime)).setText(activity.getTime().toString());
        ((TextView) view.findViewById(R.id.textViewDays)).setText(activity.getDays().toString());
    }
}