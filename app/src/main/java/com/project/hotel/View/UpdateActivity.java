package com.project.hotel.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.Room;
import com.project.hotel.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private Button close;
    private Button add;
    private EditText number;
    public EditText price;
    public EditText capacity; // вмещаемость
    public Spinner comfort;
    public EditText area; //площадь
    String[] classComfort = { "economy", "standard", "luxe"};
    String item;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_room);
        EdgeToEdge.enable(this);
        close = findViewById(R.id.btnClose);
        close.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classComfort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add=findViewById(R.id.btnReady);
        number = findViewById(R.id.editNumber);
        price = findViewById(R.id.editPrice);
        capacity = findViewById(R.id.editCapacity);
        comfort = findViewById(R.id.spClassComfort);
        comfort.setAdapter(adapter);
        area= findViewById(R.id.editArea);
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
        comfort.setOnItemSelectedListener(itemSelectedListener);

        Intent intent = getIntent();

        int number1 = intent.getIntExtra("number", 0);
        float price1 = intent.getFloatExtra("price", 0f);
        int capacity1 = intent.getIntExtra("capacity", 0);
        float area1 = intent.getFloatExtra("area", 0f);
        String comfort1 = intent.getStringExtra("comfort");

        if (comfort1 != null) {
            int spinnerPosition = adapter.getPosition(comfort1);

            comfort.setSelection(spinnerPosition);
        }

        number.setText(String.valueOf(number1));
        price.setText(String.valueOf(price1));
        capacity.setText(String.valueOf(capacity1));
        area.setText(String.valueOf(area1));

        add.setOnClickListener(v -> {
            int numField;
            float priceField;
            int capacityField;
            float areaField;
            try {
                numField  = Integer.parseInt(number.getText().toString());
                priceField = Float.parseFloat(price.getText().toString());
                capacityField  = Integer.parseInt(capacity.getText().toString());
                areaField  = Float.parseFloat(area.getText().toString());
            }catch (NumberFormatException e){
                Toast.makeText(this, "Вводите только числа.", Toast.LENGTH_SHORT).show();
                return;
            }
            MainActivity.api.check(numField).enqueue(new Callback<Boolean>() {
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
        MainActivity.api.updateRoom(room,number).enqueue(new Callback<Boolean>() {
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
