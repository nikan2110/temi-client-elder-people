package il.meuhedet.temielderpeople.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.controllers.ActivityController;
import il.meuhedet.temielderpeople.api.dto.ActivityDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import il.meuhedet.temielderpeople.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivityActivity extends AppCompatActivity {

    EditText editTextActivityTitle;
    EditText editTextActivityDescription;
    TimePicker timePickerActivityTime;
    private Map<DayOfWeek, CheckBox> dayCheckboxes;
    Retrofit retrofit = RetrofitClientTemiServer.getClient();
    ActivityController activityController = retrofit.create(ActivityController.class);
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        editTextActivityTitle = findViewById(R.id.editTextActivityTitle);
        editTextActivityDescription = findViewById(R.id.editTextActivityDescription);
        timePickerActivityTime = findViewById(R.id.timePickerActivityTime);

        // Инициализация чекбоксов дней недели
        dayCheckboxes = new HashMap<>();
        dayCheckboxes.put(DayOfWeek.SUNDAY, findViewById(R.id.checkboxSunday));
        dayCheckboxes.put(DayOfWeek.MONDAY, findViewById(R.id.checkboxMonday));
        dayCheckboxes.put(DayOfWeek.TUESDAY, findViewById(R.id.checkboxTuesday));
        dayCheckboxes.put(DayOfWeek.WEDNESDAY, findViewById(R.id.checkboxWednesday));
        dayCheckboxes.put(DayOfWeek.THURSDAY, findViewById(R.id.checkboxThursday));
        dayCheckboxes.put(DayOfWeek.FRIDAY, findViewById(R.id.checkboxFriday));
        dayCheckboxes.put(DayOfWeek.SATURDAY, findViewById(R.id.checkboxSaturday));

        Button buttonSaveActivity = findViewById(R.id.buttonSaveActivity);
        buttonSaveActivity.setOnClickListener(v -> {
            saveActivity();
        });

        Button buttonExitFromActivity = findViewById(R.id.buttonExitFromAddActivity);
        buttonExitFromActivity.setOnClickListener(v -> {
            Intent reminderIntent = new Intent(this, RemindersActivity.class);
            startActivity(reminderIntent);
        });

    }

    private void saveActivity() {
        String title = editTextActivityTitle.getText().toString();
        String description = editTextActivityDescription.getText().toString();

        // Получение времени из TimePicker
        int hour = timePickerActivityTime.getHour();
        int minute = timePickerActivityTime.getMinute();
        LocalTime time = LocalTime.of(hour, minute);

        Set<DayOfWeek> days = new HashSet<>();
        for (Map.Entry<DayOfWeek, CheckBox> entry : dayCheckboxes.entrySet()) {
            if (entry.getValue().isChecked()) {
                days.add(entry.getKey());
            }
        }
        ActivityDTO activityDTO = new ActivityDTO(title, description, time.toString(), days);

        sendDataToServer(activityDTO);

    }

    private void sendDataToServer(ActivityDTO newActivityDTO) {
//      String userId = sharedPreferencesManager.getUserId();
        String userId = "346412612";
        Log.i("userId", userId);
        activityController.createActivity(newActivityDTO, userId).enqueue(new Callback<ActivityDTO>() {

            @Override
            public void onResponse(Call<ActivityDTO> call, Response<ActivityDTO> response) {
                Log.i("responseCode", String.valueOf(response.code()));
                if (response.code() == 200) {
                    ActivityDTO activityDTO = response.body();
                    if (activityDTO != null) {
                        startActivity(new Intent(AddActivityActivity.this, RemindersActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityDTO> call, Throwable t) {
                Log.i("error, add activity", t.getMessage());
            }
        });
    }
}