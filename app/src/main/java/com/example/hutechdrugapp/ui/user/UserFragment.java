package com.example.hutechdrugapp.ui.user;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hutechdrugapp.ChangePassActivity;
import com.example.hutechdrugapp.Database.Database;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UserFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mData;
    ImageButton imgbtnChangeName;
    ArrayList<Medicine> medicines;


    private TextView txvChangePass;

    Database database;
    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        userViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //  textView.setText(s);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        database = new Database(getContext(), "product.sqlite", null, 3);
        imgbtnChangeName = root.findViewById(R.id.btn_change_name);
        imgbtnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //GetData();
                } catch (Exception e) {
                    //Log.d("tensp", e.toString());
                }

            }
        });
        // final TextView textView = root.findViewById(R.id.text_home);
//        changePass();
        return root;
    }

    //=========================================================================================================
    private void changePass() {
        txvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getContext(), ChangePassActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("newpass", e.toString());
                }

            }
        });
    }


    private void GetData() {
        Cursor dataProduct = database.GetData("SELECT TenThuoc FROM Product");
        // Toast.makeText(getContext(),n,Toast.LENGTH_LONG).show();
        // int size =dataProduct.getColumnCount();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();
        while (dataProduct.moveToNext()) {
            //  int id=dataProduct.getInt(0);
            String tensp = dataProduct.getString(0);

            arrayList.add(tensp);


        }
        for (int i = 0; i < arrayList.size(); i++) {

        }

    }

    private Query GetDataFromFireBase(String tenThuoc) {
        Query query = FirebaseDatabase.getInstance().getReference("Thuoc").orderByChild("TenThuoc").equalTo(tenThuoc);
        query.addListenerForSingleValueEvent(valueEventListener);
        return query;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            medicines.clear();
            if (snapshot.exists()) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Medicine medicine = snapshot1.getValue(Medicine.class);
                    medicines.add(new Medicine(medicine.getChiDinh(), medicine.getChongChiDinh(), medicine.getHSD(), medicine.getHinhAnh(), medicine.getHoatChat(), medicine.getNongDo(), medicine.getPhanLoai(), medicine.getTacDung(), medicine.getTenThuoc()));
                }
//                adapter.notifyDataSetChanged();
//                onStop();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }


    };
}