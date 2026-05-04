package com.project.hotel.Model;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableWork {
    TableLayout tableLayout;
    AppCompatActivity context;
    public List<Room> rooms = new ArrayList<>();

    public TableWork(TableLayout tableLayout, AppCompatActivity context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void ShowRoomsfromDB() {
        RetrofitClient.api.getAllRooms().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
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
    public void showRooms(List<Room> rooms) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"Номер", "Цена", "Вмещаемость",
                "Класс", "Площадь"});
        for (Room room : rooms) {
            TableRoomsGenerated(tableLayout, Room.RoomtoString(room));
        }
    }

    @SuppressLint("SetTextI18n")
    public void showRooms(Room room) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"Номер", "Цена", "Вмещаемость",
                "Класс", "Площадь"});
        TableRoomsGenerated(tableLayout, Room.RoomtoString(room));
    }

    public void TableRoomsGenerated(TableLayout table, String[] options) {
        TableRow row = new TableRow(context);
        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.BLACK);
        for (String str : options) {
            TextView newText = new TextView(context);
            newText.setText(str);
            newText.setGravity(Gravity.CENTER);
            row.addView(newText, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        row.setPadding(5, 5, 5, 5);
        row.setBackground(border);
        table.addView(row);
    }
}
