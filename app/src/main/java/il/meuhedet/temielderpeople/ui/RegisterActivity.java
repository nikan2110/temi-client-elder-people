package il.meuhedet.temielderpeople.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import il.meuhedet.temielderpeople.R;
import il.meuhedet.temielderpeople.api.controllers.UserController;
import il.meuhedet.temielderpeople.api.dto.UserDTO;
import il.meuhedet.temielderpeople.api.dto.UserRegistrationDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import il.meuhedet.temielderpeople.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    Retrofit retrofit = RetrofitClientTemiServer.getClient();
    UserController userController = retrofit.create(UserController.class);
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextId = findViewById(R.id.editTextId);
        Button buttonSubmitRegistration = findViewById(R.id.buttonSubmitRegistration);

        buttonSubmitRegistration.setOnClickListener(v -> {
            String identityNumber = editTextId.getText().toString();
            String name = editTextName.getText().toString();
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(identityNumber, name);
            registerUser(userRegistrationDTO);

        });

    }

    private void registerUser(UserRegistrationDTO userRegistrationDTO) {
        userController.createUser(userRegistrationDTO).enqueue(new Callback<UserDTO>() {

            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                UserDTO userDTO = response.body();
                if (userDTO != null) {
                    Toast.makeText(RegisterActivity.this,
                            "User create successfully",
                            Toast.LENGTH_SHORT).show();
                    String userId = userDTO.getIdentityNumber();
                    sharedPreferencesManager.saveUserId(userId);

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.i("registration_error", t.getMessage());
                Toast.makeText(RegisterActivity.this,
                        "Something wrong with server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}