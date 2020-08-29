package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.model.ModelLoader;
import com.example.hutechdrugapp.Database.Database;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.Model.MedicineSaved;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    TextView txvNameMedicine,txvHSD,txvTacDung,txvChiDinh,txvChongChiDinh;
    Database database;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    Button btnSaveMedicine;

    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imgMedicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Anhxa();
       // database=new Database(this,"product.sqlite",null,3);
     //   database.QueryData("DROP TABLE IF EXISTS Product");

     //   CreateTable();
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();


        LoadAnh();

        loadDetailsMedicine();
        CheckData();

        try {
            btnSaveMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    SaveData();
//                    loadData();

                }
            });

        }catch (Exception e){
            Log.d("saveDrug",e.toString());
        }


    }
    private void Anhxa(){
        imgMedicine=findViewById(R.id.imgMedicineDetails);
        txvNameMedicine=findViewById(R.id.txvNameMedicineDetails);
        txvHSD=findViewById(R.id.txvHsdDetails);
        txvTacDung=findViewById(R.id.txvtacdung);
        txvChiDinh=findViewById(R.id.txvchidinh);
        txvChongChiDinh=findViewById(R.id.txvchongchidinh);
        btnSaveMedicine=findViewById(R.id.btnLuuThuoc);

    }



    private void SaveData(){// save medicine

        Intent intent=getIntent();
        final Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ThuocDaTraCuu").orderByChild("tenThuoc").equalTo(medicine.getTenThuoc());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    Query query1=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Log.d("DaLuu","Duplicate");

                            }
                            else {
                                MedicineSaved saved=new MedicineSaved(mUser.getEmail(),medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc());
                                mData.child("ThuocDaTraCuu").push().setValue(saved);
                                btnSaveMedicine.setBackgroundColor(R.color.colorBlue);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        // do something with the individual "issues"
//                    }
                }
                else {
                    MedicineSaved saved=new MedicineSaved(mUser.getEmail(),medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc());
                    mData.child("ThuocDaTraCuu").push().setValue(saved);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // check Duplicate
    private void CheckData(){// save medicine

        Intent intent=getIntent();
        final Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ThuocDaTraCuu").orderByChild("tenThuoc").equalTo(medicine.getTenThuoc());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    Query query1=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                               // Log.d("DaLuu","Duplicate");
                                  btnSaveMedicine.setBackgroundColor(R.color.colorBlue);
                            }
                            else {
//                                MedicineSaved saved=new MedicineSaved(mUser.getEmail(),medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc());
//                                mData.child("ThuocDaTraCuu").push().setValue(saved);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        // do something with the individual "issues"
//                    }
                }
                else {
//                    MedicineSaved saved=new MedicineSaved(mUser.getEmail(),medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc());
//                    mData.child("ThuocDaTraCuu").push().setValue(saved);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void LoadAnh(){
        mData.child("Thuoc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Medicine medicine=snapshot.getValue(Medicine.class);
                Intent intent=getIntent();
                Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

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