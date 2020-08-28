package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePassActivity extends AppCompatActivity {

    EditText edtNewPass,edtEntirePassword,edtCurrentPass;
    ImageButton btnConfirm;
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
                    ".{4,10}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
        edtNewPass=findViewById(R.id.edtNewPass);
        edtEntirePassword=findViewById(R.id.editTextConfirmPassword);
        btnConfirm=findViewById(R.id.confirmnewpass);
        edtCurrentPass=findViewById(R.id.edtCurrentPass);

        try {
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( !validatePassWord() | !validateEntirePassword() | !validateCurrentPassWord()) {
                        Toast.makeText(ChangePassActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //confirmChange();
                        CheckCurrentPass();

                    }
                }
            });


        }catch (Exception e){
            Log.d("changepass",e.toString());
        }

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

    //kiem tra xac nhan password
    private boolean validateEntirePassword(){
        String EntirePasswordInput = edtEntirePassword.getText().toString().trim();
        String Password=edtNewPass.getText().toString().trim();
        if(!EntirePasswordInput.equals(Password)){
            edtEntirePassword.setError("Entire Wrong !!!");

            return false;
        }else if(EntirePasswordInput.isEmpty()){
            edtEntirePassword.setError("Field can't be empty!");
            return false;

        }
        else {

            edtEntirePassword.setError(null);
            return true;
        }
    }

    // kiem tra edittext current pass null

    private boolean validateCurrentPassWord(){
        String passwordInput = edtCurrentPass.getText().toString();
        if(passwordInput.equals("") && !passwordInput.matches("\\S")) {
            edtCurrentPass.setError("Field can't be empty!");
            return false;
        }
        else
        {
            edtCurrentPass.setError(null);
            return true;
        }
    }

    // kiem tra password hien tai

    private void CheckCurrentPass(){// check pass hien tai va thay doi pass
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuth.signInWithEmailAndPassword(mUser.getEmail(), edtCurrentPass.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                confirmChange();
                Log.d("success","success");
                Intent intent=new Intent(ChangePassActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();


            } else {
                // If sign in fails, display a message to the user.
                edtCurrentPass.setError("mat khau hien tai khong dung");

                // ...
            }
            // ...
        }
    });

    }



}