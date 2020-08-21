package com.example.hutechdrugapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(this);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onBackPressed();
        Intent myIntent = new Intent(view.getContext(), SigninActivity.class);
        startActivityForResult(myIntent, 0);
    }
}