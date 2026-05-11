package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.Entity.User;
import com.project.hotel.api.RetrofitClient;
import com.project.hotel.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        var binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogIn.setOnClickListener(v -> {
            try {
                String UserLogin = binding.EditLogin.getText().toString().trim();
                String UserPassword = binding.EditPassword.getText().toString().trim();
                if (UserLogin.isEmpty() ||  UserPassword.isEmpty())
                {
                    throw new NullPointerException();
                }
                getUser(UserLogin, UserPassword);
            }catch (NullPointerException e)
            {
                Toast.makeText(LoginActivity.this, "Введите данные",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCreate.setOnClickListener(v -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}