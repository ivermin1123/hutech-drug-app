package com.example.hutechdrugapp.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hutechdrugapp.Adapter.MedicineAdapter;
import com.example.hutechdrugapp.ChangePassActivity;
import com.example.hutechdrugapp.MapActivity;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.example.hutechdrugapp.SigninActivity;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<Medicine> medicines;
    MedicineAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference mData;

    Button btnNhaThuoc;
    private ProgressDialog progressDialog;


    private TextView txvChangePass;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mData=  FirebaseDatabase.getInstance().getReference();


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
       // final TextView textView = root.findViewById(R.id.text_home);
        txvChangePass=root.findViewById(R.id.txvChangePass);
        btnNhaThuoc=root.findViewById(R.id.btnNhaThuoc);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data ...");



//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//             //   textView.setText(s);
//            }
//        });

        RecyclerView recyclerView=root.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        medicines=new ArrayList<>();

        adapter=new MedicineAdapter(medicines,getContext());

        recyclerView.setAdapter(adapter);

        //loadData();

        btnNhaThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchNhaThuoc();
            }
        });

        // get 4 thuoc dau tien update
        try {

         Query  mData=  FirebaseDatabase.getInstance().getReference("Thuoc").limitToFirst(4);
            progressDialog.show();
            mData.addListenerForSingleValueEvent(valueEventListener);


        }catch (Exception e){
            Log.d("limit",e.toString());

        }

        return root;
    }

    private void SearchNhaThuoc(){
        Intent intent=new Intent(getContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            medicines.clear();
            if(snapshot.exists()){
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Medicine medicine=snapshot1.getValue(Medicine.class);
                    medicines.add(new Medicine(medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc()));
                }

                adapter.notifyDataSetChanged();
                onStop();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        progressDialog.dismiss();
    }


    private void loadData()
    {
        mData.child("Thuoc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Medicine medicine=snapshot.getValue(Medicine.class);
                medicines.add(new Medicine(medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc()));

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                mData= (DatabaseReference) FirebaseDatabase.getInstance().getReference("Thuoc").limitToFirst(4);
//
//                Medicine medicine=snapshot.getValue(Medicine.class);
//                medicines.add(new Medicine(medicine.getChiDinh(),medicine.getChongChiDinh(),medicine.getHSD(),medicine.getHinhAnh(),medicine.getHoatChat(),medicine.getNongDo(),medicine.getPhanLoai(),medicine.getTacDung(),medicine.getTenThuoc()));
//                adapter.notifyDataSetChanged();
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



}