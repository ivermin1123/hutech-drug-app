package com.example.hutechdrugapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutechdrugapp.DetailsActivity;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    holder.cardView.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.yellow));
                    Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("MedicineObject",list.get(position));

                    // moi lan search va select medicine thi se luu lai giu lieu da xem theo user email
                    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    DatabaseReference mData= FirebaseDatabase.getInstance().getReference();

                   // mData.child("LichSuTraCuu").child("caongocnguyen99@").setValue(mUser.getEmail());
                  //  mData.child("LichSuTraCuu").child("ThuocDaTraCuu").setValue(list.get(position));



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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txvNameDrug=itemView.findViewById(R.id.txvNameDrug);
            imgDrug=itemView.findViewById(R.id.imvDrug);
            phanloai=itemView.findViewById(R.id.txvPhanLoai);
            hsd=itemView.findViewById(R.id.txvHSD);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }


}
