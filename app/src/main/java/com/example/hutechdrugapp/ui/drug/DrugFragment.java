package com.example.hutechdrugapp.ui.drug;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hutechdrugapp.Adapter.SearchAdapter;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.example.hutechdrugapp.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrugFragment extends Fragment {

    private DrugViewModel drugViewModel;
    private RecyclerView mresult;
    private SearchView searchView;
    DatabaseReference mData;
    SearchAdapter adapter;

    ArrayList<Medicine> list;
    private DatabaseReference mMedicineDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        drugViewModel =
                ViewModelProviders.of(this).get(DrugViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drug, container, false);
       // final TextView textView = root.findViewById(R.id.text_drug);
        drugViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });


        mData=  FirebaseDatabase.getInstance().getReference();
        mMedicineDatabase= FirebaseDatabase.getInstance().getReference().child("Thuoc");

        mresult=root.findViewById(R.id.mResultList);
        searchView=root.findViewById(R.id.SearchView);
//         adapter=new SearchAdapter(list);
//        mresult.setAdapter(adapter);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mMedicineDatabase!=null)
        {
            mMedicineDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list=new ArrayList<>();

                        for (DataSnapshot ds:snapshot.getChildren()){
                            list.add(ds.getValue(Medicine.class));

                        }
                            adapter=new SearchAdapter(list);
                            mresult.setAdapter(adapter);
                      //  mresult.setAdapter(adapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
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

         adapter=new SearchAdapter(list);
        mresult.setAdapter(adapter);
    }



}