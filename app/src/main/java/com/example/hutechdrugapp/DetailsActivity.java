package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hutechdrugapp.Database.Database;
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

        database=new Database(this,"product.sqlite",null,3);
        CreateTable();
     //   database.QueryData("DROP TABLE IF EXISTS Product");

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();


        LoadAnh();

        loadDetailsMedicine();

        btnSaveMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SaveMedicine();
            }
        });
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

    private void CreateTable()
    {
        boolean tableExist=tableExists("Product");
        Log.d("AAA", String.valueOf(tableExist));

        if(!tableExist)
        {
            database.QueryData("CREATE TABLE IF NOT EXISTS Product(Id INTEGER PRIMARY KEY AUTOINCREMENT,TenThuoc VARCHAR(30),UNIQUE(TenThuoc))");
        }
        else {

        }


    }

    private void SaveMedicine(){
        String tensp=txvNameMedicine.getText().toString();
        //  String cost=edtCostSP.getText().toString();
        try {
            database.QueryData("INSERT OR REPLACE INTO Product VALUES(null,'"+tensp+"')");
            //SELECT 5, 'text to insert'
            //WHERE NOT EXISTS(SELECT 1 FROM memos WHERE id = 5 AND text = 'text to insert')
            Toast.makeText(DetailsActivity.this,"Save Success",Toast.LENGTH_LONG).show();

        }catch (Exception e){

            Log.d("BBB",e.toString());
        }
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

    boolean tableExists( String tableName)
    {
        if (tableName == null )
        {
            return false;
        }
        Cursor cursor = database.GetData("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name ='"+tableName+"'");
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}