package com.project.hotel.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.project.hotel.Model.OnRoomFoundListener;
import com.project.hotel.Model.Room;
import com.project.hotel.R;
import com.project.hotel.api.HotelApi;
import com.project.hotel.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRoomFoundListener {

    private Button btnBack;
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnFind;
    private Button btnDel;

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
        btnBack = findViewById(R.id.btnBack);
        btnAdd=findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        btnUpdate=findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            EditDialogFragment dialog = new EditDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Выбор комнаты");
            args.putString("choice", "select");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        btnFind=findViewById(R.id.btnFind);
        btnFind.setOnClickListener(v -> {
            EditDialogFragment dialog = new EditDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Поиск");
            args.putString("choice", "find");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        btnDel=findViewById(R.id.btnDel);
        btnDel.setOnClickListener(v -> {
            EditDialogFragment dialog = new EditDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Удаление");
            args.putString("choice", "delete");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });


        tableLayout = findViewById(R.id.tableRooms);
        ShowRoomsfromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowRoomsfromDB();
    }

    private void ShowRoomsfromDB() {
        RetrofitClient.api.getAllRooms().enqueue(new Callback<>() {
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
        TableRoomsGenerated(tableLayout, new String[]{"Номер", "Цена", "Вмещаемость",
        "Класс", "Площадь"});
        for (Room room : rooms) {
            TableRoomsGenerated(tableLayout,room.RoomtoString());
        }
    }

    private void TableRoomsGenerated(TableLayout table, String[] options) {
        TableRow row = new TableRow(this);
        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.BLACK);
        for (String str: options) {
            TextView newText = new TextView(this);
            newText.setText(str);
            newText.setGravity(Gravity.CENTER);
            row.addView(newText, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        row.setPadding(5, 5, 5, 5);
        row.setBackground(border);
        table.addView(row);
    }

    @Override
    public void onRoomFound(Room room) {
        btnBack.setVisibility(View.VISIBLE);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);
        showRooms(roomList);
        btnBack.setOnClickListener(v -> {
            ShowRoomsfromDB();
            btnBack.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRoomDeleted(){
        ShowRoomsfromDB();
    }

    @Override
    public void selectRoom(Room room)
    {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra("number", room.getNumber());
        intent.putExtra("price", room.getPrice());
        intent.putExtra("capacity", room.getCapacity());
        intent.putExtra("area", room.getArea());
        intent.putExtra("comfort", room.getComfort());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.LogOut) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}