package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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

import com.example.hutechdrugapp.Adapter.DrugAdapter;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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
        database=new Database(this,"product.sqlite",null,3);
       // database.QueryData("DROP TABLE IF EXISTS Product");
//        CreateTable();
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();

        //Set ViewPager 2
        drugsViewPager = findViewById(R.id.drugsViewPagerDetail);
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

        //Check duplicate
        //Neu chua luu
        btnSaveMedicine.setTag("btn_savedrug");
        //Neu luu roi
        // btnSaveMedicine.setTag("btn_deletedrug");
        btnSaveMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageName = (String) btnSaveMedicine.getTag();
                if(imageName.equals("btn_savedrug")){
                    btnSaveMedicine.setImageResource(R.drawable.btn_deletedrug);
                    btnSaveMedicine.setTag("btn_deletedrug");
                }else{
                    btnSaveMedicine.setImageResource(R.drawable.btn_savedrug);
                    btnSaveMedicine.setTag("btn_savedrug");
                }
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
        btn_back=findViewById(R.id.btn_back);
        txvDrugname=findViewById(R.id.txvDrugname);
        txvMathuoc=findViewById(R.id.tvnd1);
    }

    private void CreateTable()
    {
        boolean tableExist=tableExists("Product");
        Log.d("AAA", String.valueOf(tableExist));

        if(!tableExist)
        {
            // tao bang
            //database.QueryData("CREATE TABLE IF NOT EXISTS Product(Id INTEGER PRIMARY KEY AUTOINCREMENT,TenSP VARCHAR(100),Gia DOUBLE)");
            database.QueryData("CREATE TABLE IF NOT EXISTS Product(Id INTEGER PRIMARY KEY AUTOINCREMENT,TenThuoc VARCHAR(100))");
        }
        else {

        }
    }

    private void SaveData(){
        String tensp=txvNameMedicine.getText().toString();
        //  String cost=edtCostSP.getText().toString();
        try {
            database.QueryData("INSERT  INTO Product(Id,TenThuoc) SELECT null,'"+tensp+"' WHERE NOT EXISTS(SELECT TenThuoc FROM Product WHERE TenThuoc='"+tensp+"')");
//OR REPLAC
        }catch (Exception e){
            Log.d("BBB",e.toString());
        }
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