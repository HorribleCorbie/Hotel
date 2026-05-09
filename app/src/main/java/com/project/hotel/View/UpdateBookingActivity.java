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
import com.project.hotel.Model.User;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;
import com.project.hotel.databinding.BookingsBinding;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBookingActivity extends AppCompatActivity {

    BookingsBinding binding;

    private long bookingId;
    private int currentRoomNumber;
    private String currentInDate;
    private String currentOutDate;
    private long clientId;

    private LocalDate firstdate;
    private LocalDate enddate;
    private int selectedRoomNumber;
    private float priceDays;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = BookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.titleBooking.setText(R.string.update_info);
        binding.titleBooking.setEnabled(true);

        Intent intent = getIntent();
        bookingId = intent.getLongExtra("id", 0);
        currentRoomNumber = intent.getIntExtra("room", 0);
        currentInDate = intent.getStringExtra("in_date");
        currentOutDate = intent.getStringExtra("out_date");
        clientId = intent.getLongExtra("client", 0);

        showCurrentBookingInfo();

        setupDatePicker();

        binding.btnBackBooking.setOnClickListener(v -> {
            startActivity(new Intent(UpdateBookingActivity.this, AdminBookingActivity.class));
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showCurrentBookingInfo() {
        binding.selectedDates.setVisibility(View.VISIBLE);
        binding.selectedDates.setText("Текущие даты: " + currentInDate + " - " + currentOutDate);

        binding.selectedRoom.setVisibility(View.VISIBLE);

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Текущая комната: " + currentRoomNumber});
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectedRoom.setAdapter(roomAdapter);

        binding.btnBooking.setText("Обновить бронь");
        binding.btnBooking.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    private void setupDatePicker() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate bookingDate = LocalDate.parse(currentInDate, dtf);
        LocalDate today = LocalDate.now();

        LocalDate minLimit = today.isBefore(bookingDate) ? today : bookingDate;
        long minLimitMs = minLimit.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        CalendarConstraints calendarConstraintBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.from(minLimitMs))
                .build();

        MaterialDatePicker<Pair<Long, Long>> rangeDatePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setCalendarConstraints(calendarConstraintBuilder)
                .setTitleText("Выберите новые даты")
                .setSelection(new Pair<>(bookingDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), null))
                .build();

        binding.btnSelectTime.setOnClickListener(v -> rangeDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        rangeDatePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedIn = Instant.ofEpochMilli(selection.first).atZone(ZoneOffset.UTC).toLocalDate();
            LocalDate selectedOut = Instant.ofEpochMilli(selection.second).atZone(ZoneOffset.UTC).toLocalDate();

            String finalInStr;

            if (bookingDate.isBefore(today)) {
                finalInStr = currentInDate;
                Toast.makeText(this, "Дата заезда защищена (бронь в прошлом)", Toast.LENGTH_SHORT).show();
            } else {
                finalInStr = selectedIn.format(dtf);
            }

            String finalOutStr = selectedOut.format(dtf);

            binding.selectedRoom.setVisibility(View.VISIBLE);
            binding.selectedDates.setText("Период: " + finalInStr + " — " + finalOutStr);
            binding.titleBooking.setText(R.string.choice_room);

            selectingRooms(rangeDatePicker);
        });
    }


    private void selectingRooms(MaterialDatePicker<Pair<Long, Long>> rangeDatePicker) {
        long startDate = rangeDatePicker.getSelection().first;
        long endDate = rangeDatePicker.getSelection().second;

        firstdate = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        enddate = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();

        binding.titleBooking.setText("Выберите подходящий номер.");

        RetrofitClient.api.allRoomsforBookings(firstdate, enddate, bookingId).enqueue(new Callback<List<Room>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        List<Room> rooms = response.body();
                        TableWork table = new TableWork(binding.availableRooms, UpdateBookingActivity.this);
                        table.showRooms(rooms);

                        List<Integer> roomNumbers = Room.RoomstoList(rooms);

                        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                                UpdateBookingActivity.this,
                                android.R.layout.simple_spinner_item,
                                roomNumbers);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.selectedRoom.setAdapter(adapter);

                        int defaultPosition = roomNumbers.indexOf(currentRoomNumber);
                        if (defaultPosition != -1) {
                            binding.selectedRoom.setSelection(defaultPosition);
                            selectedRoomNumber = currentRoomNumber;
                        }

                        binding.selectedRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedRoomNumber = (int) parent.getItemAtPosition(position);
                                long msDiff = endDate - startDate;
                                int daysDiff = (int) TimeUnit.MILLISECONDS.toDays(msDiff) + 1;
                                priceDays = Room.GetRoomPrice(rooms, selectedRoomNumber) * daysDiff;
                                binding.finalPrice.setText("Стоимость проживания: " + priceDays);
                                binding.finalPrice.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        binding.btnBooking.setEnabled(true);

                        binding.btnBooking.setOnClickListener(v -> {
                                    RetrofitClient.Userapi.getUser(clientId).enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                User user = response.body();
                                                updateBooking(user.getLogin());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {

                                        }
                                    });

                                }

                        );

                    } catch (RuntimeException e) {
                        Toast.makeText(UpdateBookingActivity.this, "Неизвестная ошибка.", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateBooking", "Error: ", e);
                    }
                } else {
                    Toast.makeText(UpdateBookingActivity.this, "Нет свободных комнат.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(UpdateBookingActivity.this, "Ошибка соединения.", Toast.LENGTH_SHORT).show();
                Log.e("UpdateBooking", "API Failure: ", t);
            }
        });
    }

    private void updateBooking(String login) {
        if (selectedRoomNumber == 0) {
            Toast.makeText(this, "Выберите комнату", Toast.LENGTH_SHORT).show();
            return;
        }

        BookingRequest updatedBooking = new BookingRequest(selectedRoomNumber, login, firstdate, enddate, java.lang.String.valueOf(priceDays));
        RetrofitClient.Bookingapi.updateBooking(bookingId, updatedBooking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdateBookingActivity.this, "Бронирование успешно обновлено!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateBookingActivity.this, AdminBookingActivity.class));
                    finish();
                } else {
                    Toast.makeText(UpdateBookingActivity.this, "Ошибка при обновлении бронирования", Toast.LENGTH_SHORT).show();
                    Log.e("UpdateBooking", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(UpdateBookingActivity.this, "Ошибка соединения: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateBooking", "Failure: ", t);
            }
        });
    }
}