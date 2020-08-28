package com.example.hutechdrugapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hutechdrugapp.Adapter.SearchAdapter;
import com.example.hutechdrugapp.Model.Medicine;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView mresult;
    private SearchView searchView;
    DatabaseReference mData;

    ArrayList<Medicine>list;


    private DatabaseReference mMedicineDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AnhXa();
        mMedicineDatabase= FirebaseDatabase.getInstance().getReference().child("Thuoc");
    }

    //========================================================
    private void AnhXa(){
//        edtSearch=findViewById(R.id.edtSearchMedicine);
//        imgSearch=findViewById(R.id.ivsearch);

        mresult=findViewById(R.id.mResultList);
        searchView=findViewById(R.id.SearchView);

    }
    //=========================================================


    //=======================================================

    @Override
    protected void onStart() {
        super.onStart();
        if(mMedicineDatabase!=null);
        {
            mMedicineDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list=new ArrayList<>();

                        for (DataSnapshot ds:snapshot.getChildren()){
                            list.add(ds.getValue(Medicine.class));

                        }

                        SearchAdapter adapter=new SearchAdapter(list);
                        mresult.setAdapter(adapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        if(searchView!=null){

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {

                        search(newText);

                    }catch (Exception e){
                        Log.d("listdrug",e.toString());
                    }

                    return true;
                }
            });
        }

    }

    private void search(String s){
        ArrayList<Medicine>mylist=new ArrayList<>();

        for (Medicine object: list){
            if(object.getTenThuoc().toLowerCase().contains(s.toLowerCase()))
            {
                mylist.add(object);
            }

        }

        SearchAdapter adapter=new SearchAdapter(mylist);
        mresult.setAdapter(adapter);
    }




}