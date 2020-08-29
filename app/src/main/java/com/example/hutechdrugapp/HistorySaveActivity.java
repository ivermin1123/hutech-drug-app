//package com.example.hutechdrugapp;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.hutechdrugapp.Adapter.MedicineSavedAdapter;
//import com.example.hutechdrugapp.Adapter.SearchAdapter;
//import com.example.hutechdrugapp.Database.Database;
//import com.example.hutechdrugapp.Model.Medicine;
//import com.example.hutechdrugapp.Model.MedicineSaved;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
//public class HistorySaveActivity extends AppCompatActivity {
//
//    private RecyclerView mresult;
//    private SearchView searchView;
//    DatabaseReference mData;
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;
//
//    ArrayList<MedicineSaved> list;
//    Database database;
//
//    private DatabaseReference mMedicineDatabase;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history_save);
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//        mData=  FirebaseDatabase.getInstance().getReference();
//     //   mMedicineDatabase= FirebaseDatabase.getInstance().getReference();
//
//        AnhXa();
//
//
//
//      //  mMedicineDatabase= FirebaseDatabase.getInstance().getReference().child("Thuoc");
//
//
//
//
//
//      //  loadData();
//        try {
//            Query query=FirebaseDatabase.getInstance().getReference("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());
//            query.addListenerForSingleValueEvent(valueEventListener);
//
//        }  catch (Exception e)
//        {
//            Log.d("GetSaved",e.toString());
//        }
//
//    }
//
//    private void AnhXa(){
////        edtSearch=findViewById(R.id.edtSearchMedicine);
////        imgSearch=findViewById(R.id.ivsearch);
//
//        mresult=findViewById(R.id.mResultList);
////        mresult.setHasFixedSize(true);
////        mresult.setLayoutManager(new LinearLayoutManager(this));
////        list=new ArrayList<>();
//
//
//       // searchView=findViewById(R.id.SearchView);
//
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if(mData!=null);
//        {
//            mData.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        list=new ArrayList<>();
//
//                        for (DataSnapshot ds:snapshot.getChildren()){
//                            list.add(ds.getValue(MedicineSaved.class));
//
//                        }
//
//                        MedicineSavedAdapter adapter=new MedicineSavedAdapter(list);
//                        mresult.setAdapter(adapter);
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(HistorySaveActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//    }
//
//    ValueEventListener valueEventListener=new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            list.clear();
//            if(snapshot.exists()){
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    MedicineSaved medicineSaved=snapshot1.getValue(MedicineSaved.class);
//                    list.add(medicineSaved);
//                }
//                MedicineSavedAdapter adapter=new MedicineSavedAdapter(list);
//                mresult.setAdapter(adapter);
//
//            }
//
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    };
//
//
//}