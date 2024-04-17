package il.meuhedet.temielderpeople.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import il.meuhedet.temielderpeople.R;

public class LoginActivityOrRegister extends AppCompatActivity {

//    private AppDatabase db;
//    private ExecutorService databaseExecutor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        buttonLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });




//        db = AppDatabase.getDatabase(this);
//        databaseExecutor = Executors.newSingleThreadExecutor();
    }
}