package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hutechdrugapp.Model.Medicine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    TextView txvNameMedicine,txvHSD,txvTacDung,txvChiDinh,txvChongChiDinh;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imgMedicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Anhxa();

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();

//        Intent intent=getIntent();
//        Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");
        //  Toast.makeText(DetailsActivity.this,medicine.getHinhAnh(),Toast.LENGTH_LONG).show();
        LoadAnh();

        loadDetailsMedicine();
    }
    private void Anhxa(){
        imgMedicine=findViewById(R.id.imgMedicineDetails);
        txvNameMedicine=findViewById(R.id.txvNameMedicineDetails);
        txvHSD=findViewById(R.id.txvHsdDetails);
        txvTacDung=findViewById(R.id.txvtacdung);
        txvChiDinh=findViewById(R.id.txvchidinh);
        txvChongChiDinh=findViewById(R.id.txvchongchidinh);
    }

    private void LoadAnh(){
        mData.child("Thuoc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Medicine medicine=snapshot.getValue(Medicine.class);
                Intent intent=getIntent();
                Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");
                Toast.makeText(DetailsActivity.this,medicine.getHinhAnh(),Toast.LENGTH_LONG).show();
                Picasso.get().load(medicine.getHinhAnh()).into(imgMedicine);

                //  imgMedicine.setImageResource(medicine.getHinhAnh().);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDetailsMedicine(){
        Intent intent=getIntent();
        Medicine  medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        txvNameMedicine.setText(medicine.getTenThuoc());
        txvHSD.setText(medicine.getHSD());
        txvTacDung.setText(medicine.getTacDung());
        txvChiDinh.setText(medicine.getChiDinh());
        txvChongChiDinh.setText(medicine.getChongChiDinh());



    }
}