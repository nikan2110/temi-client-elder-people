package il.meuhedet.temielderpeople.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.constants.SdkConstants;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.controllers.ActivityController;
import il.meuhedet.temielderpeople.api.controllers.ChatGPTController;
import il.meuhedet.temielderpeople.api.dto.ActivityDTO;
import il.meuhedet.temielderpeople.api.dto.ChatGPTAnswerDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientChatGPTServer;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import il.meuhedet.temielderpeople.utils.ReminderBroadcastReceiver;
import il.meuhedet.temielderpeople.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RemindersActivity extends AppCompatActivity
        implements OnRobotReadyListener, Robot.AsrListener, Robot.TtsListener {

    private LinearLayout activitiesContainer;
    Retrofit retrofitTemiServer = RetrofitClientTemiServer.getClient();
    Retrofit retrofitChatGPT = RetrofitClientChatGPTServer.getClient();
    ActivityController activityController = retrofitTemiServer.create(ActivityController.class);
    ChatGPTController chatGPTController = retrofitChatGPT.create(ChatGPTController.class);
    SharedPreferencesManager sharedPreferencesManager;
    Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        robot = Robot.getInstance();

        sharedPreferencesManager = new SharedPreferencesManager(this);

        activitiesContainer = findViewById(R.id.activitiesContainer);

        Button buttonAddActivity = findViewById(R.id.buttonAddActivity);
//        Button buttonBackToLogin = findViewById(R.id.buttonBackToLogin);
        Button buttonTemiInteraction = findViewById(R.id.buttonTemiInteraction);

        buttonAddActivity.setOnClickListener(v -> {
            startActivity(new Intent(RemindersActivity.this, AddActivityActivity.class));
        });

//        buttonBackToLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Очищаем стек активностей
//            startActivity(intent);
//        });

        buttonTemiInteraction.setOnClickListener(v -> {
            robot.askQuestion("Start GPT mode. Ask your question.");
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
                    + activity.getId().intValue() * 100 + day.getValue() + " at "
                    + calendar.getTime());

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
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
            View activityView = LayoutInflater.from(this).inflate(R.layout.activity_item,
                    activitiesContainer, false);
            populateActivityView(activityView, activity);
            activitiesContainer.addView(activityView);
        }
    }

    private void populateActivityView(View view, ActivityDTO activity) {
        ((TextView) view.findViewById(R.id.textViewTitle)).setText(activity.getTitle());
        ((TextView) view.findViewById(R.id.textViewDescription)).setText(activity.getDescription());
        ((TextView) view.findViewById(R.id.textViewTime)).setText(activity.getTime());
        ((TextView) view.findViewById(R.id.textViewDays)).setText(activity.getDays().toString());
    }

    @Override
    public void onAsrResult(@NonNull String asrResult) {
        Log.i("asrResult", asrResult);
        try {
            ApplicationInfo metadata = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            if (metadata.metaData == null) {
                return;
            }
            if (!robot.isSelectedKioskApp()) {
                robot.requestToBeKioskApp();
            }
            if (!metadata.metaData.getBoolean(SdkConstants.METADATA_OVERRIDE_NLU)) {
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if (!asrResult.isEmpty()) {
            HashMap<String, String> body = new HashMap<>();
            body.put("question", asrResult);
            sendQuestion(body);
        }
    }

    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        Log.i("tts status", ttsRequest.getStatus().toString());
        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            robot.wakeup();
        }
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
        robot.addAsrListener(this);
        robot.addTtsListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);
        robot.removeAsrListener(this);
        robot.removeTtsListener(this);
    }


    private void sendQuestion(Map<String, String> body) {
        chatGPTController.askQuestion(body).enqueue(new Callback<ChatGPTAnswerDTO>() {

            @Override
            public void onResponse(Call<ChatGPTAnswerDTO> call, Response<ChatGPTAnswerDTO> response) {
                ChatGPTAnswerDTO chatGPTAnswerModel = response.body();
                if (chatGPTAnswerModel != null && !chatGPTAnswerModel.getResponse().isEmpty()) {
                    String chatGPTAnswerModelText = chatGPTAnswerModel.getResponse();
                    robot.speak(TtsRequest.create(chatGPTAnswerModelText, true));
                }
            }

            @Override
            public void onFailure(Call<ChatGPTAnswerDTO> call, Throwable t) {
                Toast.makeText(RemindersActivity.this,
                        "Something wrong with server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}