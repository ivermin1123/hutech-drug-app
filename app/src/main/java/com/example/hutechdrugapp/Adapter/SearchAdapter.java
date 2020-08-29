package com.example.hutechdrugapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutechdrugapp.DetailsActivity;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.Model.MedicineSaved;
import com.example.hutechdrugapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<Medicine> list;

   // Context context;
    public SearchAdapter(ArrayList<Medicine> list){
        this.list=list;
      //  this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.itemView.setTag(list.get(position));
        holder.txvNameDrug.setText(list.get(position).getTenThuoc());
        //Glide.with().load(list.get(position).getHinhAnh()).into(holder.imgDrug);
        Picasso.get().load(list.get(position).getHinhAnh()).into(holder.imgDrug);
        holder.phanloai.setText(list.get(position).getPhanLoai());
        holder.hsd.setText(list.get(position).getHSD());

        //holder.cardView.setCardBackgroundColor(.getResources().getColor(R.color.colorPrimaryLight));


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("ThuocDaTraCuu").orderByChild("tenThuoc").equalTo(list.get(position).getTenThuoc());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                   FirebaseUser mUser=mAuth.getCurrentUser();
                    Query query1=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // Log.d("DaLuu","Duplicate");

                                holder.btnSave.setImageResource(R.drawable.btn_deletedrug);
                                holder.btnSave.setTag("btn_deletedrug");
                                //  DeleteData();

                            }
                            else {

                                holder.btnSave.setImageResource(R.drawable.btn_savedrug);
                                holder.btnSave.setTag("btn_savedrug");
                                holder.btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                                        final FirebaseUser mUser=mAuth.getCurrentUser();
                                        final DatabaseReference  mData= FirebaseDatabase.getInstance().getReference();

                                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                        Query query=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());

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

                                                                MedicineSaved saved=new MedicineSaved(mUser.getEmail(),list.get(position).getChiDinh(),list.get(position).getChongChiDinh(),list.get(position).getHSD(),list.get(position).getHinhAnh(),list.get(position).getHoatChat(),list.get(position).getNongDo(),list.get(position).getPhanLoai(),list.get(position).getTacDung(),list.get(position).getTenThuoc());
                                                                mData.child("ThuocDaTraCuu").push().setValue(saved);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });

                                                }
                                                else {
                                                    MedicineSaved saved=new MedicineSaved(mUser.getEmail(),list.get(position).getChiDinh(),list.get(position).getChongChiDinh(),list.get(position).getHSD(),list.get(position).getHinhAnh(),list.get(position).getHoatChat(),list.get(position).getNongDo(),list.get(position).getPhanLoai(),list.get(position).getTacDung(),list.get(position).getTenThuoc());
                                                    mData.child("ThuocDaTraCuu").push().setValue(saved);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    // check data
                    holder.btnSave.setImageResource(R.drawable.btn_savedrug);
                    holder.btnSave.setTag("btn_savedrug");
                    holder.btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                            final FirebaseUser mUser=mAuth.getCurrentUser();
                            final DatabaseReference  mData= FirebaseDatabase.getInstance().getReference();

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                            Query query=reference.child("ThuocDaTraCuu").orderByChild("email").equalTo(mUser.getEmail());

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

                                                    MedicineSaved saved=new MedicineSaved(mUser.getEmail(),list.get(position).getChiDinh(),list.get(position).getChongChiDinh(),list.get(position).getHSD(),list.get(position).getHinhAnh(),list.get(position).getHoatChat(),list.get(position).getNongDo(),list.get(position).getPhanLoai(),list.get(position).getTacDung(),list.get(position).getTenThuoc());
                                                    mData.child("ThuocDaTraCuu").push().setValue(saved);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                                    }
                                    else {
                                        MedicineSaved saved=new MedicineSaved(mUser.getEmail(),list.get(position).getChiDinh(),list.get(position).getChongChiDinh(),list.get(position).getHSD(),list.get(position).getHinhAnh(),list.get(position).getHoatChat(),list.get(position).getNongDo(),list.get(position).getPhanLoai(),list.get(position).getTacDung(),list.get(position).getTenThuoc());
                                        mData.child("ThuocDaTraCuu").push().setValue(saved);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
// check data

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   // holder.cardView.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.yellow));
                    Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("MedicineObject",list.get(position));




                    view.getContext().startActivity(intent);

                }catch (Exception e){
                    Log.d("NNNN",e.toString());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txvNameDrug,phanloai,hsd;
        ImageView imgDrug;
        CardView cardView;
        ImageButton btnSave;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txvNameDrug=itemView.findViewById(R.id.txvNameDrug);
            imgDrug=itemView.findViewById(R.id.imvDrug);
            phanloai=itemView.findViewById(R.id.txvPhanLoai);
            hsd=itemView.findViewById(R.id.txvHSD);
            cardView=itemView.findViewById(R.id.cardview);
            btnSave=itemView.findViewById(R.id.btn_saveDrug);
        }
    }


}
