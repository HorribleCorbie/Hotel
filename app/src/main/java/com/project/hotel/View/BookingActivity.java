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
import androidx.core.util.Pair;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.project.hotel.Model.Booking;
import com.project.hotel.Model.BookingRequest;
import com.project.hotel.Model.Room;
import com.project.hotel.Model.TableWork;
import com.project.hotel.api.RetrofitClient;
import com.project.hotel.databinding.BookingsBinding;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
    //https://www.geeksforgeeks.org/android/material-design-date-picker-in-android/
    //https://www.geeksforgeeks.org/android/more-functionalities-of-material-design-date-picker-in-android/
    //https://stackoverflow.com/questions/35183146/how-can-i-create-a-java-8-localdate-from-a-long-epoch-time-in-milliseconds
    //https://stackoverflow.com/questions/62739054/how-to-count-number-of-days-between-date-range-material-picker-in-andr
    //https://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist

    private BookingsBinding binding;
    private int id_room;
    private LocalDate firstdate;
    private LocalDate enddate;
    private float priceDays;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = BookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBooking.setOnClickListener(v -> {
            listenerCreationNewBooking();
        });

        CalendarConstraints calendarConstraintBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now()).build();

        MaterialDatePicker<Pair<Long, Long>> rangeDatePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setCalendarConstraints(calendarConstraintBuilder)
                .setTitleText("Выберите дату")
                .build();

        binding.btnSelectTime.setOnClickListener(v -> rangeDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        rangeDatePicker.addOnPositiveButtonClickListener(selection -> {
            binding.selectedDates.setVisibility(View.VISIBLE);
            binding.selectedRoom.setVisibility(View.VISIBLE);
            binding.selectedDates.setText("Выбранные даты : " + rangeDatePicker.getHeaderText());
            binding.titleBooking.setText("Выберите подходящий номер.");
            selectingRooms(rangeDatePicker);
        });

        binding.btnBackBooking.setOnClickListener(v -> {
            startActivity(new Intent(BookingActivity.this, MainClientActivity.class));
            finish();
        });
    }

    private void listenerCreationNewBooking() {
        BookingRequest createBooking = new BookingRequest(id_room, MainClientActivity.client.getLogin(), firstdate,
                enddate, String.valueOf(priceDays));
        RetrofitClient.Bookingapi.newBooking(createBooking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(BookingActivity.this, "Бронирование успешно создано.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookingActivity.this, MainClientActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Ошибка на стороне сервера, попробуйте позже.", Toast.LENGTH_SHORT).show();
                Log.e("BookingAPI", "ON FAILURE: ", t);
            }
        });
    }

    private void selectingRooms(MaterialDatePicker<Pair<Long, Long>> rangeDatePicker) {
        long startDate = rangeDatePicker.getSelection().first;
        long endDate = rangeDatePicker.getSelection().second;

        firstdate = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        enddate = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();

        binding.titleBooking.setText("Выберите подходящий номер.");
        RetrofitClient.api.allRoomsforBookings(firstdate, enddate).enqueue(new Callback<List<Room>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        List<Room> rooms = response.body();
                        TableWork table = new TableWork(binding.availableRooms, BookingActivity.this);
                        table.showRooms(rooms);
                        List<Integer> numbers = Room.RoomstoList(rooms);

                        ArrayAdapter<Integer> adapter = new ArrayAdapter(
                                BookingActivity.this, android.R.layout.simple_spinner_item, numbers);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.selectedRoom.setAdapter(adapter);
                        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                id_room = (int) parent.getItemAtPosition(position);
                                long msDiff = endDate - startDate;
                                int daysDiff = (int) TimeUnit.MILLISECONDS.toDays(msDiff) + 1;
                                priceDays = Room.GetRoomPrice(rooms, id_room) * daysDiff;
                                binding.finalPrice.setText("Стоимость проживания: " + priceDays);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        };

                        binding.selectedRoom.setOnItemSelectedListener(itemSelectedListener);
                        binding.finalPrice.setVisibility(View.VISIBLE);
                        binding.btnBooking.setEnabled(true);
                    } catch (RuntimeException e) {
                        Toast.makeText(BookingActivity.this, "Неизвестная ошибка.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingActivity.this, "Нет свободных комнат.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Ошибка соединения.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}