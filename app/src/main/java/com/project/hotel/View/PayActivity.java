package com.project.hotel.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.hotel.Model.Entity.Booking;
import com.project.hotel.api.RetrofitClient;
import com.project.hotel.databinding.PaymentBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    PaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = PaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(PayActivity.this, MainClientActivity.class);
            intent.putExtra("need_pay", true);
            intent.putExtra("delete", false);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });
        binding.checkPayment.setOnClickListener(v -> {
            listenerCreationNewBooking();
        });
    }
    private void listenerCreationNewBooking() {
        RetrofitClient.Bookingapi.newBooking(MainClientActivity.unpaid).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                Intent intent = new Intent(PayActivity.this, MainClientActivity.class);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PayActivity.this, "Бронирование успешно оплачено.", Toast.LENGTH_SHORT).show();
                    intent.putExtra("need_pay", false);
                }else {
                    Toast.makeText(PayActivity.this, "Комната была уже забронирована.", Toast.LENGTH_SHORT).show();
                    intent.putExtra("need_pay", false);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(PayActivity.this, "Комната была уже забронирована.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, MainClientActivity.class);
                intent.putExtra("need_pay", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                Log.e("BookingAPI", "ON FAILURE: ", t);
            }
        });
    }
}
