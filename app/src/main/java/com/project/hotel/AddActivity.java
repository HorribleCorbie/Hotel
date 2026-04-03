package com.project.hotel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.Room;

public class AddActivity extends AppCompatActivity {

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
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
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
        add.setOnClickListener(v -> {
            int numField  = Integer.parseInt(number.getText().toString());
            float priceField = Float.parseFloat(price.getText().toString());
            int capacityField  = Integer.parseInt(capacity.getText().toString());
            float areaField  = Float.parseFloat(area.getText().toString());
            if (MainActivity.api.check(numField))
            {
                Room room = new Room(numField,priceField,capacityField, item, areaField );
            }

        });

    }
}