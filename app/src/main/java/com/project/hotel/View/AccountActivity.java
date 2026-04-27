package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.TextWatcherAdapter;
import com.project.hotel.Model.User;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity  extends AppCompatActivity {

    Button create;
    Button back;
    EditText user;
    EditText login;
    EditText password;
    TextView title;

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

        title = findViewById(R.id.txt_title);
        title.setText("Ваш аккаунт");
        Log.e("MY_API_ERROR", "help");

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
        Log.e("MY_API_ERROR", MainClientActivity.client.getName() + " " + MainClientActivity.client.getPassword()
        + " " + MainClientActivity.client.getLogin());
        user.setText(MainClientActivity.client.getName());
        password.setText(MainClientActivity.client.getPassword());
        login.setText(MainClientActivity.client.getLogin());

        login.setEnabled(false);
        Log.e("MY_API_ERROR", "help");
        create.setOnClickListener(v -> {
            String name = String.valueOf(user.getText());
            String pass = String.valueOf(password.getText());
            if (name.isEmpty() || name.length() < 2 || name.length() >= 255) {
                Toast.makeText(this, "Имя должно быть не пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty() || pass.length() < 4 || pass.length() >= 255) {
                Toast.makeText(this, "Пароль должен содержать больше 4 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Objects.equals(MainClientActivity.client.getName(), name) && Objects.equals(MainClientActivity.client.getPassword(), pass)){
                Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            MainClientActivity.client.setName(name);
            MainClientActivity.client.setPassword(pass);
            RetrofitClient.Userapi.updateUser(MainClientActivity.client, MainClientActivity.client.getLogin()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() !=null){
                        Toast.makeText(AccountActivity.this, "Данные успешно обновленны", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(AccountActivity.this,
                            "Соединение нестабильно.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
        back.setOnClickListener(v -> startActivity(new Intent(AccountActivity.this, MainClientActivity.class)));
    }

}
