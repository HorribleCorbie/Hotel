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
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableWork extends Table{
    public List<Room> rooms = new ArrayList<>();

    public TableWork(TableLayout tableLayout, AppCompatActivity context) {
        super(tableLayout, context);
    }

    public void showAllFromDB() {
        RetrofitClient.api.getAllRooms().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rooms = response.body();
                    rooms.sort(Comparator.comparing(Room::getNumber));
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

}
