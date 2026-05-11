package com.project.hotel.Model;

import static com.project.hotel.Model.Entity.Booking.BookingClientToString;
import static com.project.hotel.Model.Entity.Booking.BookingToString;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.Entity.Booking;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingTable extends Table {
    public List<Booking> bookings = new ArrayList<>();

    private int isActive = 0;
    private LocalDate checkInDate = null;
    private LocalDate checkOutDate = null;

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    private Long clientID = null;

    public Long getClientID() {
        return clientID;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void restartCheckInDate() {
        checkInDate = null;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void restartCheckOutDate() {
        checkOutDate = null;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void restartClientID() {
        clientID = null;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public BookingTable(TableLayout tableLayout, AppCompatActivity context) {
        super(tableLayout, context);
    }

    public void showAllFromDB() {
        RetrofitClient.Bookingapi.getAllBookings().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookings = response.body();
                    bookings.sort(Comparator.comparing(Booking::getId));
                    if (isActive != 0 || checkInDate != null || checkOutDate != null || clientID != null) {
                        filterBookings();
                    } else {
                        showBookingsForAdmin(bookings);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Log.e("MY_API_ERROR", "Ошибка запроса: " + t.getMessage());
            }
        });
    }

    public void filterBookings() {
        List<Booking> sortList = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (Booking booking : bookings) {
            LocalDate outDate = LocalDate.parse(booking.getOut_date());

            if (isActive == 1) {
                if (now.isAfter(outDate) || now.isEqual(outDate)) continue;
            } else if (isActive == 2) {
                if (now.isBefore(outDate) || now.isEqual(outDate)) continue;
            }

            if (checkInDate != null) {
                LocalDate inDate = LocalDate.parse(booking.getIn_date());
                if (checkInDate.isAfter(inDate)) continue;
            }

            if (checkOutDate != null) {
                if (checkOutDate.isBefore(outDate)) continue;
            }

            if (clientID != null && !Objects.equals(clientID, booking.getClient().getId())) {
                continue;
            }
            sortList.add(booking);
        }
        showBookingsForAdmin(sortList);
    }

    public long[] getIdClient() {
        return bookings.stream()
                .mapToLong(b -> b.getClient().getId())
                .distinct()
                .toArray();
    }

    @SuppressLint("SetTextI18n")
    public void showBookingsForAdmin(List<Booking> bookings) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"ID", "Номер", "Клиент",
                "Въезд", "Выезд", "Цена"});
        for (Booking booking : bookings) {
            TableRoomsGenerated(tableLayout, BookingToString(booking));
        }
    }

    @SuppressLint("SetTextI18n")
    public void showBookingsForAdmin(Booking booking) {
        tableLayout.removeAllViews();
        TableRoomsGenerated(tableLayout, new String[]{"ID", "Номер", "Клиент",
                "Въезд", "Выезд", "Цена"});
        TableRoomsGenerated(tableLayout, BookingToString(booking));
    }

    public void showAllBookingsByClient(Long id, TextView text) {
        RetrofitClient.Bookingapi.getAllBookingsByClients(id).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookings = response.body();
                    if (!bookings.isEmpty()) {
                        showRoomsForClient(bookings);
                        text.setText(
                                R.string.txt_if_user_have_bookings);
                    }
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
        }
    }
}
