package com.example.hutechdrugapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;

    EditText edtUserName,edtPassword;
    TextView txtSignup,txtForgotPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        AnhXa();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog.setMessage("Login ...");
        //Loading

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
                Intent myIntent = new Intent(SigninActivity.this, RegisterActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
        // forgot password
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SigninActivity.this, ForgotpwdActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);;
            }
        });

        //=============================================================

        ImageButton btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUserName() | !validatePassWord()) {
                    Toast.makeText(SigninActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    DangNhap();
                }
            }
        });



    }
    //==============================================================================================================================

    private void AnhXa(){
        edtPassword=(EditText)findViewById(R.id.editTextPassword);
        edtUserName=(EditText)findViewById(R.id.editTextUsername);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgotPass=findViewById(R.id.txtForgot);
        progressDialog = new ProgressDialog(SigninActivity.this);

    }


    private boolean validateUserName(){
        String usernameInput = edtUserName.getText().toString().trim();
        if(usernameInput.isEmpty()){
            edtUserName.setError("Field can't be empty!");
            return false;
        }else {
//            textInputUserName.setError(null);
            edtUserName.setError(null);
            return true;
        }
    }
    // Kiểm tra PassWord
    private boolean validatePassWord(){
        String passwordInput = edtPassword.getText().toString().trim();
        if(passwordInput.isEmpty()) {
            edtPassword.setError("Field can't be empty!");
            return false;
        }
        else
        {
            edtPassword.setError(null);
            return true;
        }
    }

//====================================================================================================================================
    private void DangNhap()
    {
        String email=edtUserName.getText().toString();
        String password=edtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SigninActivity.this,"Sai Tên Đăng Nhập Hoặc Mật Khẩu!!!",Toast.LENGTH_SHORT).show();
                            onStop();
                            // ...
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
    }

    //==============================================================================================================

}