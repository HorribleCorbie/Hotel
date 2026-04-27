package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.TextWatcherAdapter;
import com.project.hotel.Model.User;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button create;
    Button back;
    EditText user;
    EditText login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);

        create = findViewById(R.id.btnCreateUser);
        back = findViewById(R.id.btnBackReg);
        user = findViewById(R.id.editName);
        login = findViewById(R.id.editLogIn);
        password = findViewById(R.id.editPassReg);

        user.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {

                String original = s.toString().trim();
                String newstr = original.toLowerCase();

                StringBuilder newText = new StringBuilder();

                if (!original.isEmpty()) {
                    char prev_c = newstr.charAt(0);
                    newText.append(Character.toUpperCase(prev_c));
                    char[] chars = newstr.substring(1).toCharArray();

                    for (char c : chars) {
                        if (prev_c == ' ') {
                            newText.append(Character.toUpperCase(c));
                        } else newText.append(c);
                        prev_c = c;
                    }

                    String processed = newText.toString();
                    if (!original.equals(processed)) {
                        user.removeTextChangedListener(this);
                        user.setText(processed);
                        user.setSelection(processed.length());
                        user.addTextChangedListener(this);
                    }
                }
            }
        });


        create.setOnClickListener(v -> {
            String name = String.valueOf(user.getText());
            String pass = String.valueOf(password.getText());
            String log = String.valueOf(login.getText());
            if (name.isEmpty() || name.length() < 2 || name.length() >= 255) {
                Toast.makeText(this, "Имя должно быть не пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty() || pass.length() < 4 || pass.length() >= 255) {
                Toast.makeText(this, "Пароль должен содержать больше 4 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (log.isEmpty() || log.length() < 4 || log.length() >= 255) {
                Toast.makeText(this, "Логин должен содержать больше 4 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser =new User(log, pass, name, "client");

            RetrofitClient.Userapi.checkLogin(log).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() !=null){
                        if (!response.body()){
                            CreateNewUser(newUser);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,
                                    "Пользователь с таким логином уже существует",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "Соединение нестабильно.",
                            Toast.LENGTH_SHORT).show();
                }
            });

        });

        back.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    public void CreateNewUser(User user){
        RetrofitClient.Userapi.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() !=null)
                {
                    Toast.makeText(RegisterActivity.this,
                            "Пользователь создан.",
                            Toast.LENGTH_LONG).show();
                    MainClientActivity.client = user;
                    startActivity(new Intent(RegisterActivity.this, MainClientActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,
                        "Соединение нестабильно.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
