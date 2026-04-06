package com.project.hotel.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.project.hotel.Model.OnRoomFoundListener;
import com.project.hotel.Model.Room;
import com.project.hotel.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDialogFragment extends DialogFragment {

    private OnRoomFoundListener Found;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Found = (OnRoomFoundListener) context;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        String choice = getArguments().getString("choice");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        EditText edit = view.findViewById(R.id.editdialog);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());


        return builder
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    int number;
                    try {
                        number = Integer.parseInt(edit.getText().toString());
                        existingRoom(number, choice);
                    }catch (NumberFormatException e){
                        return;
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }

    private void existingRoom(int number, String choice) {
        MainActivity.api.check(number).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body()!=null)
                {
                    if(response.body())
                    {
                        switch (choice)
                        {
                            case "find":
                                outputRoom(number);
                                break;
                            case "delete":
                                deleteRoom(number);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    private void deleteRoom(int number){
        MainActivity.api.deleteRoom(number).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Found.onRoomDeleted();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void outputRoom(int number) {
        MainActivity.api.getRoom(number).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    Room room = response.body();
                    Found.onRoomFound(room);
                }
            }
            @Override
            public void onFailure(Call<Room> call, Throwable t) {

            }
        });
    }
}
