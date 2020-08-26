package com.example.hutechdrugapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<Medicine> list;

    //Context context;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txvNameDrug.setText(list.get(position).getTenThuoc());
        //Glide.with().load(list.get(position).getHinhAnh()).into(holder.imgDrug);
        Picasso.get().load(list.get(position).getHinhAnh()).into(holder.imgDrug);
        holder.phanloai.setText(list.get(position).getPhanLoai());
        holder.hsd.setText(list.get(position).getHSD());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txvNameDrug,phanloai,hsd;
        ImageView imgDrug;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txvNameDrug=itemView.findViewById(R.id.txvNameDrug);
            imgDrug=itemView.findViewById(R.id.imvDrug);
            phanloai=itemView.findViewById(R.id.txvPhanLoai);
            hsd=itemView.findViewById(R.id.txvHSD);
        }
    }
}
