package com.example.hutechdrugapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtPassword,edtEntirePassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ImageButton btnRegist;
    CheckBox checkBox;


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
        setContentView(R.layout.activity_register);

        AnhXa();

        TextView txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(this);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        //========================== xac nhan dang ky ================================

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !validatePassWord() | !validateEmail() | !validateEntirePassword()){

                    Toast.makeText(RegisterActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                    if(edtEntirePassword.getText()!=edtPassword.getText()){
//                        Toast.makeText(SingupActivity.this, "Mật Khẩu Nhập Lại Sai", Toast.LENGTH_SHORT).show();
//
//                    }
                }else if (!checkBox.isChecked()){
                    Toast.makeText(RegisterActivity.this, "vui long doc terms and conditions", Toast.LENGTH_SHORT).show();
                }
                else {
                    DangKy();
                }
            }
        });
        //=================================xac nhan dang ky=============================================
    }

//=========================================================================================
    @Override
    public void onClick(View view) {
        super.onBackPressed();
        Intent myIntent = new Intent(view.getContext(), SigninActivity.class);
        startActivityForResult(myIntent, 0);
    }

    private void AnhXa(){
        edtEmail=findViewById(R.id.editTextEmail);

        edtPassword=findViewById(R.id.editTextPassword);
        edtEntirePassword=findViewById(R.id.editTextConfirmPassword);
       btnRegist=findViewById(R.id.imgButtonRegist);
       checkBox=findViewById(R.id.checkBox);

        // khoi tao authen firebase
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
    }
//=========================================================================================
private void DangKy(){
    String email=edtEmail.getText().toString();
    String password=edtPassword.getText().toString();
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Sign in success, update UI with the signed-in user's information
                    if(task.isSuccessful())
                    {
//<<<<<<< HEAD
//                        Toast.makeText(RegisterActivity.this,"Dang Ky Thanh Cong",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
//                        startActivity(intent);
//                        mAuth.signOut();
//=======
                        final FlatDialog flatDialog = new FlatDialog(RegisterActivity.this);
                        flatDialog.setTitle("Success")
                                .setSubtitle("Dang Ky Thanh Cong.")
                                .setFirstButtonText("OK")
                                .withFirstButtonListner(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(RegisterActivity.this, SigninActivity.class);
                                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(myIntent);
                                        mAuth.signOut();
                                    }
                                })
                                .show();

                    }else {
                        Toast.makeText(RegisterActivity.this,"Email da ton tai!!!",Toast.LENGTH_SHORT).show();
                    }
                }

            });
}

    //================================================================================

    private boolean validateEmail(){
        String emailInput=edtEmail.getText().toString().trim();
        if(emailInput.isEmpty()){
            edtEmail.setError("Field can't be empty!");
            return false;
        }
        else {
            edtEmail.setError(null);
            return true;
        }
    }
    private boolean validatePassWord(){
        String passwordInput = edtPassword.getText().toString();
        if(passwordInput.equals("") && !passwordInput.matches("\\S")) {
            edtPassword.setError("Field can't be empty!");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            edtPassword.setError("Password too weak!,space not allowed");
            return false;
        }
        else
        {
            edtPassword.setError(null);
            return true;
        }
    }
    //
    //kiem tra xac nhan password
    private boolean validateEntirePassword(){
        String EntirePasswordInput = edtEntirePassword.getText().toString().trim();
        String Password=edtPassword.getText().toString().trim();
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

    //validate checkbox


    //=============================================================================
}