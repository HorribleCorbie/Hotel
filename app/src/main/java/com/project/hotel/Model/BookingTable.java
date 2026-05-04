package com.project.hotel.Model;

import static com.project.hotel.Model.Booking.BookingClientToString;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingTable {
    TableLayout tableLayout;
    AppCompatActivity context;
    public List<Booking> bookings = new ArrayList<>();

    public BookingTable(TableLayout tableLayout, AppCompatActivity context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

//    public void showAllBookings() {
//        RetrofitClient.Bookingapi.getAllBookings().enqueue(new Callback<List<Booking>>() {
//            @Override
//            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    bookings = response.body();
//                    showRooms(bookings);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Booking>> call, Throwable t) {
//
//            }
//        });
//    }

    public void showAllBookingsByClient(Long id, TextView text) {
        RetrofitClient.Bookingapi.getAllBookingsByClients(id).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookings = response.body();
                    if (!bookings.isEmpty()){
                    showRoomsForClient(bookings);
                    text.setText(R.string.txt_if_user_have_bookings);}
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Log.e("BookingAPI", "ON FAILURE: ", t);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showRoomsForClient(List<Booking> bookings) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"Номер комнаты", "Цена", "Дата въезда",
                "Дата Выезда"});
        for (Booking booking : bookings) {
            TableRoomsGenerated(tableLayout, BookingClientToString(booking));
            tableLayout.invalidate();
            tableLayout.requestLayout();
        }
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
