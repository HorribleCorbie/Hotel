package com.project.hotel.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.hotel.Model.Booking;
import com.project.hotel.Model.OnBookingFoundListener;
import com.project.hotel.Model.OnRoomFoundListener;
import com.project.hotel.Model.Room;
import com.project.hotel.R;
import com.project.hotel.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookingDialogFragment extends DialogFragment {

    private OnBookingFoundListener Found;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Found = (OnBookingFoundListener) context;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        String choice = getArguments().getString("choice");

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog, null);
        EditText edit = view.findViewById(R.id.editdialog);
        TextView text = view.findViewById(R.id.txt_dialog);
        text.setText(R.string.edit_id);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());


        return builder
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    long id;
                    try {
                        id = Long.parseLong(edit.getText().toString());
                        existingBooking(id, choice);
                    }catch (NumberFormatException e){
                        return;
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }

    private void showErrorToast() {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Бронирования не существует", Toast.LENGTH_SHORT).show();
        }
    }

    private void existingBooking(Long id, String choice) {
        RetrofitClient.Bookingapi.check(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body()!=null)
                {
                    try {
                        if (response.body()) {
                            switch (choice) {
                                case "find":
                                    outputBooking(id);
                                    break;
                                case "delete":
                                    deleteBooking(id);
                                    break;
                                case "select":
                                    selectBooking(id);
                                    break;
                            }
                        } else {
                            showErrorToast();
                        }
                    }catch (NullPointerException e){
                        showErrorToast();
                    }
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    private void deleteBooking(Long id){
        RetrofitClient.Bookingapi.deleteBooking(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Found.onBookingDeleted();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void selectBooking(Long id){
        RetrofitClient.Bookingapi.getBooking(id).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    Booking booking = response.body();
                    Found.selectBooking(booking);
                }
            }
            @Override
            public void onFailure(Call<Booking> call, Throwable t) {

            }
        });
    }
    private void outputBooking(Long id){
        RetrofitClient.Bookingapi.getBooking(id).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    Booking booking = response.body();
                    Found.onBookingFound(booking);
                }
            }
            @Override
            public void onFailure(Call<Booking> call, Throwable t) {

            }
        });
    }
}
