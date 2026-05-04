package com.project.hotel.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.TableWork;
import com.project.hotel.Model.User;
import com.project.hotel.R;

public class MainClientActivity extends AppCompatActivity {

    static User client;
    TableLayout tablelayoute;
    TableWork table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.client_main);
        tablelayoute = findViewById(R.id.table_clients);
        table = new TableWork(tablelayoute, this);
        table.ShowRoomsfromDB();
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
        } else {
            startActivity(new Intent(MainClientActivity.this, AccountActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}