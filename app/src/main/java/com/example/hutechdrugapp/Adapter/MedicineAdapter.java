package com.example.hutechdrugapp.Adapter;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    ArrayList<Medicine> medicines;
    Context context;

    public MedicineAdapter(ArrayList<Medicine> medicines, Context context) {
        this.medicines = medicines;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.item_row,parent,false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setTag(medicines.get(position));
        holder.txtName.setText(medicines.get(position).getTenThuoc());
//        Picasso.get().load(medicines.get(position).getHinhAnh()).into(holder.imgHinh);
        Glide.with(context).load(medicines.get(position).getHinhAnh()).into(holder.imgHinh);
        //holder.cardview2.setCardBackgroundColor(R.color.colorTrendingStart);
        holder.cardview2.setBackgroundResource(R.drawable.card_view_border);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                //    holder.cardview2.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryLight));
                    Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("MedicineObject",medicines.get(position));
                    //

                    view.getContext().startActivity(intent);

                }catch (Exception e){
                    Log.d("NNNN",e.toString());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        ImageView imgHinh;
        CardView cardview2;
      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          txtName=itemView.findViewById(R.id.txtMedicineName);
          imgHinh=itemView.findViewById(R.id.imgMedicine);
          cardview2=itemView.findViewById(R.id.cardview2);
      }
  }
}
