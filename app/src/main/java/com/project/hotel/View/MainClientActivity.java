package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.project.hotel.Model.BookingTable;
import com.project.hotel.databinding.ClientMainBinding;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.TableWork;
import com.project.hotel.Model.User;
import com.project.hotel.R;

public class MainClientActivity extends AppCompatActivity {

    static User client;
    BookingTable bookingTable;
    ClientMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ClientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TableWork table = new TableWork(binding.tableClients, this);
        bookingTable = new BookingTable(binding.activeBookings, this );
        table.ShowRoomsfromDB();
        bookingTable.showAllBookingsByClient(client.getId(), binding.txtBooking);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookingTable.showAllBookingsByClient(client.getId(), binding.txtBooking);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.LogOut) {
            startActivity(new Intent(MainClientActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.booking) {
            startActivity(new Intent(MainClientActivity.this, BookingActivity.class));
            finish();
        } else if (id == R.id.account) {
            startActivity(new Intent(MainClientActivity.this, AccountActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}