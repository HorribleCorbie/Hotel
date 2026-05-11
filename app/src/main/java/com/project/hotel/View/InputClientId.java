package com.project.hotel.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.DialogFragment;

import com.project.hotel.Model.Interface.OnCLientListener;
import com.project.hotel.databinding.DialogClientBinding;

import java.util.ArrayList;
import java.util.List;


public class InputClientId extends DialogFragment {

    private OnCLientListener Found;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Found = (OnCLientListener) context;
    }

    DialogClientBinding binding;
    private long selectedId = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DialogClientBinding.inflate(getLayoutInflater());

        int button = getArguments().getInt("id");
        long[] list = getArguments().getLongArray("list");

        List<Long> idList = new ArrayList<>();
        if (list != null) {
            for (long l : list) idList.add(l);
        }

        ArrayAdapter<Long> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                idList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClient.setAdapter(adapter);

        binding.spinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedId = (long) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return new AlertDialog.Builder(requireContext())
                .setTitle("Фильтр по клиенту")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(binding.getRoot())
                .setPositiveButton("OK", (dialog, which) -> {
                    Found.selectClient(selectedId, button);
                })
                .setNegativeButton("Отмена", null)
                .create();
    }

}
