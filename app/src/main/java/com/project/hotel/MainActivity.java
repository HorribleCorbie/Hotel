package com.project.hotel;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.hotel.Model.Room;
import com.project.hotel.api.HotelApi;
import com.project.hotel.api.RetrofitClient;
import java.util.List;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private Button btnUpdate;
    private Button btnFind;
    private Button btnDel;

    HotelApi api;
    //говорим Retrofit создать объект, который поможет приложению отправлять запросы и получать ответы от API.
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd=findViewById(R.id.btnAdd);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnFind=findViewById(R.id.btnFind);
        btnDel=findViewById(R.id.btnDel);
        tableLayout = findViewById(R.id.tableRooms);
        api = RetrofitClient.getInstance().create(HotelApi.class);
        api.getAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Room> rooms = response.body();
                    showRooms(rooms);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showRooms(List<Room> rooms) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"ID","Number", "Price", "Capacity",
        "Comfort", "Area"});
        for (Room room : rooms) {
            TableRoomsGenerated(tableLayout, new String[]{String.valueOf(room.id),String.valueOf(room.number), String.valueOf(room.price),
                    String.valueOf(room.capacity), room.comfort,
                    String.valueOf(room.area)});
        }
    }

    private void TableRoomsGenerated(TableLayout table, String[] options) {
        TableRow row = new TableRow(this);
        for (String str: options) {
            TextView newText = new TextView(this);
            newText.setText(str);
            newText.setGravity(Gravity.CENTER);
            GradientDrawable border = new GradientDrawable();
            border.setStroke(1, Color.BLACK);
            newText.setBackground(border);
            row.addView(newText, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        table.addView(row);
    }
}