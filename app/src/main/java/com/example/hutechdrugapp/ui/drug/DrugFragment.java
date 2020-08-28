package com.example.hutechdrugapp.ui.drug;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hutechdrugapp.R;

public class DrugFragment extends Fragment {

    private DrugViewModel drugViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        drugViewModel =
                ViewModelProviders.of(this).get(DrugViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drug, container, false);
        final TextView textView = root.findViewById(R.id.text_drug);
        drugViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}