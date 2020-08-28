package com.example.hutechdrugapp.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hutechdrugapp.ChangePassActivity;
import com.example.hutechdrugapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView txvChangePass;


    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        userViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        // final TextView textView = root.findViewById(R.id.text_home);
//        changePass();
        return root;
    }
//=========================================================================================================
    private void changePass(){
        txvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(getContext(), ChangePassActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Log.d("newpass",e.toString());
                }

            }
        });
    }
}