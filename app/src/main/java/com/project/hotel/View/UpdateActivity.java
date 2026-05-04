package com.project.hotel.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.databinding.AddNewRoomBinding;

import com.project.hotel.Model.Room;
import com.project.hotel.api.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    String[] classComfort = { "economy", "standard", "luxe"};
    String item;
    AddNewRoomBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddNewRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        binding.btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classComfort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spClassComfort.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                item = (String)parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        binding.spClassComfort.setOnItemSelectedListener(itemSelectedListener);

        Intent intent = getIntent();

        int number1 = intent.getIntExtra("number", 0);
        float price1 = intent.getFloatExtra("price", 0f);
        int capacity1 = intent.getIntExtra("capacity", 0);
        float area1 = intent.getFloatExtra("area", 0f);
        String comfort1 = intent.getStringExtra("comfort");

        if (comfort1 != null) {
            int spinnerPosition = adapter.getPosition(comfort1);

            binding.spClassComfort.setSelection(spinnerPosition);
        }

        binding.editNumber.setText(String.valueOf(number1));
        binding.editPrice.setText(String.valueOf(price1));
        binding.editCapacity.setText(String.valueOf(capacity1));
        binding.editArea.setText(String.valueOf(area1));

        binding.btnReady.setOnClickListener(v -> {
            int numField;
            float priceField;
            int capacityField;
            float areaField;
            try {
                numField  = Integer.parseInt(binding.editNumber.getText().toString());
                priceField = Float.parseFloat(binding.editPrice.getText().toString());
                capacityField  = Integer.parseInt(binding.editCapacity.getText().toString());
                areaField  = Float.parseFloat(binding.editArea.getText().toString());
                if (numField>100 || numField<=0){
                    Toast.makeText(this, "Номер должен быть от 1 до 100.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (priceField>500 || priceField<30){
                    Toast.makeText(this, "Цена за номер должна быть в диапазоне от 30 до 500.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (areaField>150 || areaField<20){
                    Toast.makeText(this, "Площадь номера должна быть в диапазоне от 20 до 150.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (capacityField>4 || capacityField<=0){
                    Toast.makeText(this, "Вместимость номера должна быть в диапазоне от 1 до 4.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch (NumberFormatException e){
                Toast.makeText(this, "Вводите только числа.", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitClient.api.check(numField).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() !=null)
                    {
                        boolean isRoomExist = response.body();
                        if (!isRoomExist || number1==numField)
                        {
                            Room room = new Room(numField,priceField,capacityField, item, areaField );
                            MethodUpdateRoom(room, numField);
                        }
                    }
                    else {
                        Toast.makeText(UpdateActivity.this,
                                "Комната уже существует",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("MY_API_ERROR", "Ошибка запроса: " + t.getMessage());
                }
            });
        });

    }

    private void MethodUpdateRoom(Room room, int number) {
        RetrofitClient.api.updateRoom(room,number).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() !=null)
                {
                    if (response.body()){
                        Toast.makeText(UpdateActivity.this,
                                "Комната успешно изменена!",
                                Toast.LENGTH_LONG).show();
                        finish();}
                    else {
                        Toast.makeText(UpdateActivity.this,
                                "Ошибка изменения",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("MY_API_ERROR", "Ошибка запроса: " + t.getMessage());
            }
        });
  }
}
