package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.hotel.Model.User;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button Create;
    private Button LogIn;
    private EditText LoginUser;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Create = findViewById(R.id.btnCreate);
        LogIn = findViewById(R.id.btnLogIn);
        LoginUser = findViewById(R.id.EditLogin);
        Password = findViewById(R.id.EditPassword);

        LogIn.setOnClickListener(v -> {
            try {
                String UserLogin = LoginUser.getText().toString().trim();
                String UserPassword = Password.getText().toString().trim();
                if (UserLogin.isEmpty() || UserPassword.isEmpty())
                {
                    throw new NullPointerException();
                }
                getUser(UserLogin,UserPassword );
            }catch (NullPointerException e)
            {
                Toast.makeText(LoginActivity.this, "Введите данные",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Create.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    public void getUser(String login, String Pass) {

        RetrofitClient.Userapi.getUserByLogin(login).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Такого аккаунта не существует.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = response.body();

                if (user == null) {
                    Toast.makeText(LoginActivity.this,
                            "Пустой ответ сервера",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if (user.getPassword().equals(Pass)) {

                    if ("admin".equals(user.getRole())) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        MainClientActivity.client = new User(user);
                        startActivity(new Intent(LoginActivity.this, MainClientActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Неверный пароль.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Ошибка сети.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}