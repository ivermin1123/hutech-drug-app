package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.example.hutechdrugapp.Adapter.DrugAdapter;

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
    TextView txvNameMedicine,txvHSD,txvTacDung,txvChiDinh,txvChongChiDinh, txvDrugname, txvMathuoc;
    Database database;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ImageButton btnSaveMedicine;

    ImageButton btn_back;

    private ViewPager2 drugsViewPager;

    ArrayList<Medicine> medicines;
    DrugAdapter adapter;

    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imgMedicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Anhxa();


       // database.QueryData("DROP TABLE IF EXISTS Product");
//        CreateTable();

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();

        //Set ViewPager 2
        drugsViewPager = findViewById(R.id.drugsViewPagerDetail);
        drugsViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        medicines = new ArrayList<>();
        Intent intent=getIntent();
        Medicine  medicine= (Medicine) intent.getSerializableExtra("MedicineObject");
        medicines.add(medicine);
        adapter = new DrugAdapter(medicines, 0);

        loadDetailsMedicine();

        LoadAnh();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        CheckData();

    }
    private void Anhxa(){
        txvNameMedicine=findViewById(R.id.txvNameMedicineDetails);
        txvHSD=findViewById(R.id.txvHsdDetails);
        txvTacDung=findViewById(R.id.txvtacdung);
        txvChiDinh=findViewById(R.id.txvchidinh);
        txvChongChiDinh=findViewById(R.id.txvchongchidinh);
        btnSaveMedicine=findViewById(R.id.btnLuuThuoc);
        btn_back=findViewById(R.id.btn_back);
        txvDrugname=findViewById(R.id.txvDrugname);
        txvMathuoc=findViewById(R.id.tvnd1);
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
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

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

    private void DeleteData(){// delete medicine

        Intent intent=getIntent();
        final Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ThuocDaTraCuu").orderByChild("tenThuoc").equalTo(medicine.getTenThuoc());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    final Query query1=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Log.d("DaLuu","Duplicate");
                               // reference.child("tenThuoc").getRoot().removeValue();

                            }
                            else {


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    // check Duplicate
    private void CheckData(){

        Intent intent=getIntent();
        final Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ThuocDaTraCuu").orderByChild("tenThuoc").equalTo(medicine.getTenThuoc());

        query.addValueEventListener(new ValueEventListener() {
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

                                btnSaveMedicine.setImageResource(R.drawable.btn_deletedrug);
                                btnSaveMedicine.setTag("btn_deletedrug");
                                DeleteData();

                            }
                            else {

                                btnSaveMedicine.setImageResource(R.drawable.btn_savedrug);
                                btnSaveMedicine.setTag("btn_savedrug");
                                btnSaveMedicine.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SaveData();
                                    }
                                });

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
                    btnSaveMedicine.setImageResource(R.drawable.btn_savedrug);
                    btnSaveMedicine.setTag("btn_savedrug");
                    btnSaveMedicine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SaveData();
                        }
                    });
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
        drugsViewPager.setAdapter(adapter);
        drugsViewPager.setClipToPadding(false);
        drugsViewPager.setClipChildren(false);
        drugsViewPager.setOffscreenPageLimit(1);
        drugsViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        drugsViewPager.setPageTransformer(compositePageTransformer);
    }

    private void loadDetailsMedicine(){
        Intent intent=getIntent();
        Medicine  medicine= (Medicine) intent.getSerializableExtra("MedicineObject");

        assert medicine != null;
        txvDrugname.setText(medicine.getTenThuoc());
        txvMathuoc.setText(medicine.getTenThuoc());
        txvNameMedicine.setText("Hoatchat?");
        txvHSD.setText(medicine.getHSD());

        txvTacDung.setText(medicine.getTacDung());
        txvChiDinh.setText(medicine.getChiDinh());
        txvChongChiDinh.setText(medicine.getChongChiDinh());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}