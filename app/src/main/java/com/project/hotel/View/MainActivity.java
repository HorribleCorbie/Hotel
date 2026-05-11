package com.project.hotel.View;

import com.project.hotel.Model.TableWork;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.project.hotel.databinding.ActivityMainBinding;
import com.project.hotel.Model.Interface.OnRoomFoundListener;
import com.project.hotel.Model.Entity.Room;
import com.project.hotel.R;


public class MainActivity extends AppCompatActivity implements OnRoomFoundListener {
    TableWork table;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        binding.btnUpdate.setOnClickListener(v -> {
            EditRoomDialogFragment dialog = new EditRoomDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Выбор комнаты");
            args.putString("choice", "select");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        binding.btnFind.setOnClickListener(v -> {
            EditRoomDialogFragment dialog = new EditRoomDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Поиск");
            args.putString("choice", "find");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        binding.btnDel.setOnClickListener(v -> {
            EditRoomDialogFragment dialog = new EditRoomDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Удаление");
            args.putString("choice", "delete");
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");
        });

        table = new TableWork( binding.tableRooms,  this);
        table.showAllFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        table.showAllFromDB();
    }

    @Override
    public void onRoomFound(Room room) {
        binding.btnBack.setVisibility(View.VISIBLE);
        table.showRooms(room);
        binding.btnBack.setOnClickListener(v -> {
            table.showRooms(table.rooms);
            binding.btnBack.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRoomDeleted(){
        table.showAllFromDB();
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
            finish();
            return true;
        }else if (id == R.id.admin_bookings){
            startActivity(new Intent(MainActivity.this, AdminBookingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}