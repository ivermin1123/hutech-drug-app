package com.example.hutechdrugapp.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hutechdrugapp.Adapter.DrugAdapter;
import com.example.hutechdrugapp.Adapter.MedicineAdapter;
import com.example.hutechdrugapp.ChangePassActivity;
import com.example.hutechdrugapp.MapActivity;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.example.hutechdrugapp.SigninActivity;
import com.example.hutechdrugapp.ui.user.UserFragment;
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
    DrugAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference mData;
    CardView cardProfilePicture;

    Button btnNhaThuoc;
    private ProgressDialog progressDialog;


    private TextView txvChangePass, txvNameUser;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mData = FirebaseDatabase.getInstance().getReference();

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        // final TextView textView = root.findViewById(R.id.text_home);
        txvChangePass = root.findViewById(R.id.txvChangePass);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data ...");
        txvNameUser = root.findViewById(R.id.txtNameuser);
        cardProfilePicture = root.findViewById(R.id.cardProfilePicture);

        //onclick profile
        cardProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_user);
            }
        });

        //set name
        String nameUser = mUser.getDisplayName();
        if (nameUser != null) txvNameUser.setText(String.format("Xin chào, %s", nameUser));
        else {
            txvNameUser.setText(R.string.xinChao);
        }
        //Set ViewPager 2
        ViewPager2 drugsViewPager = root.findViewById(R.id.drugsViewPager);

        medicines = new ArrayList<>();
        adapter = new DrugAdapter(medicines);

        drugsViewPager.setAdapter(adapter);
        drugsViewPager.setClipToPadding(false);
        drugsViewPager.setClipChildren(false);
        drugsViewPager.setOffscreenPageLimit(3);
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

        // get 4 thuoc dau tien update
        try {
            Query mData = FirebaseDatabase.getInstance().getReference("Thuoc").limitToFirst(4);
            progressDialog.show();
            mData.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e) {
            Log.d("limit", e.toString());
        }
        return root;
    }

    private void SearchNhaThuoc() {
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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


    private void loadData() {
        mData.child("Thuoc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Medicine medicine = snapshot.getValue(Medicine.class);
                medicines.add(new Medicine(medicine.getChiDinh(), medicine.getChongChiDinh(), medicine.getHSD(), medicine.getHinhAnh(), medicine.getHoatChat(), medicine.getNongDo(), medicine.getPhanLoai(), medicine.getTacDung(), medicine.getTenThuoc()));

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