package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hutechdrugapp.ui.user.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePassActivity extends AppCompatActivity {

    EditText edtNewPass;
    ImageButton btnConfirm, btnBack;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +          //no white spaces
                    ".{6,20}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        edtNewPass=findViewById(R.id.edtNewPass);
        btnConfirm=findViewById(R.id.confirmnewpass);
        btnBack=findViewById(R.id.btnBack) ;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !validatePassWord()) {

                    Toast.makeText(ChangePassActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();

                }
                else {
                    confirmChange();
                }
            }
        });

    }
    private void confirmChange(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String  newPass=edtNewPass.getText().toString();

        mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "User password updated.");
                }
            }
        });

    }
    private boolean validatePassWord(){
        String passwordInput = edtNewPass.getText().toString();
        if(passwordInput.equals("") && !passwordInput.matches("\\S")) {
            edtNewPass.setError("Field can't be empty!");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            edtNewPass.setError("Password too weak!,space not allowed");
            return false;
        }
        else
        {
            edtNewPass.setError(null);
            return true;
        }
    }
}