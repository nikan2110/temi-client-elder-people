package il.meuhedet.temielderpeople.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.dto.UserRegistrationDTO;
import il.meuhedet.temielderpeople.utils.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        EditText editTextLoginId = findViewById(R.id.editTextLoginId);



        buttonLogin.setOnClickListener(v -> {
            String identityNumber = editTextLoginId.getText().toString();
            sharedPreferencesManager.saveUserId(identityNumber);
            Intent reminderIntent = new Intent(this, RemindersActivity.class);
            startActivity(reminderIntent);

        });



    }
}