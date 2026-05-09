package com.project.hotel.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.project.hotel.Model.Booking;
import com.project.hotel.Model.BookingTable;
import com.project.hotel.Model.OnBookingFoundListener;
import com.project.hotel.Model.OnCLientListener;
import com.project.hotel.R;
import com.project.hotel.databinding.AdminBookingsBinding;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class AdminBookingActivity extends AppCompatActivity implements OnBookingFoundListener, OnCLientListener {
    AdminBookingsBinding binding;
    BookingTable table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        table = new BookingTable(binding.adminBookings, this);
        table.showAllFromDB();

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnFilter.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        navFilter();

        binding.btnUpdate.setOnClickListener(v -> {
            EditBookingDialogFragment dialog = new EditBookingDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Редактирование");
            args.putString("choice", "select");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        binding.btnFind.setOnClickListener(v -> {
            EditBookingDialogFragment dialog = new EditBookingDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Поиск");
            args.putString("choice", "find");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        binding.btnDel.setOnClickListener(v -> {
            EditBookingDialogFragment dialog = new EditBookingDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Удаление");
            args.putString("choice", "delete");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        table.showAllFromDB();
        navFilter();
    }

    private void navFilter() {
        Menu menu = binding.NavFilter.getMenu();

        MenuItem activeItem = menu.findItem(R.id.switch_active_bookings);
        View activeView = activeItem.getActionView();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch activeSwitch = activeView.findViewById(R.id.toggle_sw);

        activeItem = menu.findItem(R.id.switch_closed_bookings);
        activeView = activeItem.getActionView();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch closedSwitch = activeView.findViewById(R.id.toggle_sw);

        activeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (closedSwitch.isChecked()) {
                    closedSwitch.setChecked(false);
                }
                table.setIsActive(1);
            } else {
                table.setIsActive(0);
            }
            table.filterBookings();
        });

        closedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (activeSwitch.isChecked()) {
                    activeSwitch.setChecked(false);
                }
                table.setIsActive(2);
            } else {
                table.setIsActive(0);
            }
            table.filterBookings();
        });

        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        final MaterialDatePicker.Builder materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        materialDatePickerBuilder.setTitleText("SELECT A DATE");
        materialDatePickerBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

        binding.NavFilter.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.client_filter) {
                InputClientId dialog = new InputClientId();
                Bundle args = new Bundle();
                args.putLongArray("list", table.getIdClient());
                args.putInt("id", id);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");
            } else {
                materialDatePicker.clearOnPositiveButtonClickListeners();
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(o -> {
                    LocalDate checkInDate = Instant.ofEpochMilli((Long) o).atZone(ZoneId.systemDefault()).toLocalDate();
                    addSwitchToMenu(checkInDate, id);
                });
            }
            menuItem.setEnabled(false);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    void addSwitchToMenu(LocalDate date, int idButton) {
        Menu menu = binding.NavFilter.getMenu();

        MenuItem itemButton = menu.findItem(idButton);
        MenuItem newItem = menu.add(R.id.add_switch_menu, Menu.NONE, Menu.NONE, itemButton.getTitle());

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switch1 = new Switch(this);
        switch1.setChecked(true);
        newItem.setActionView(switch1);
        newItem.setCheckable(true);
        if (date!=null)
            switch1.setText(date.toString());
        else
        {
            Long clientId = table.getClientID();
            if (clientId != null) {
                switch1.setText(clientId.toString());
            }
        }

        final int itemId = newItem.getItemId();

        if (idButton == R.id.check_in) {
            table.setCheckInDate(date);
        } else if (idButton == R.id.check_out) {
            table.setCheckOutDate(date);
        }

        table.filterBookings();
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                menu.removeItem(itemId);
                itemButton.setEnabled(true);

                if (idButton == R.id.check_in) {
                    table.restartCheckInDate();
                } else if (idButton == R.id.check_out) {
                    table.restartCheckOutDate();
                } else {
                    table.restartClientID();
                }

                table.filterBookings();
                binding.NavFilter.invalidate();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_admin_booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout_booking) {
            startActivity(new Intent(AdminBookingActivity.this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookingFound(Booking booking) {
        binding.btnBackAdmin.setVisibility(View.VISIBLE);
        table.showBookingsForAdmin(booking);
        binding.btnBackAdmin.setOnClickListener(v -> {
            table.showBookingsForAdmin(table.bookings);
            binding.btnBackAdmin.setVisibility(View.GONE);
        });
    }

    @Override
    public void onBookingDeleted() {
        table.showAllFromDB();
    }

    @Override
    public void selectBooking(Booking booking) {
        Intent intent = new Intent(this, UpdateBookingActivity.class);
        intent.putExtra("id", booking.getId());
        intent.putExtra("room", booking.getRoom().getNumber());
        intent.putExtra("client", booking.getClient().getId());
        intent.putExtra("in_date", booking.getIn_date());
        intent.putExtra("out_date", booking.getOut_date());
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void selectClient(Long id, int idButton) {
        table.setClientID(id);
        addSwitchToMenu(null, idButton);
    }

}

