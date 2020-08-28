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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.BufferedReader;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private ImageButton imgLoginGG;
//    private GoogleSignInOptions gso;
    EditText edtUserName,edtPassword;
    TextView txtSignup,txtForgotPass;
    private GoogleSignInClient mGoogleSignInClient;

    private final static int RC_SIGN_IN=123;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user=mAuth.getCurrentUser();
//        if(user!=null)
//        {
//            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
//            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }
//    }

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



        //==============================================================

        createRequest();

        imgLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



    }
    //==============================================================================================================================

    private void AnhXa(){
        edtPassword=(EditText)findViewById(R.id.editTextPassword);
        edtUserName=(EditText)findViewById(R.id.editTextUsername);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgotPass=findViewById(R.id.txtForgot);
        imgLoginGG=findViewById(R.id.imgbtnGG);
        progressDialog = new ProgressDialog(SigninActivity.this);

    }

    private void createRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("fgg", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                           Toast.makeText(SigninActivity.this,"Loi Dang Nhap",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
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