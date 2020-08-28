package com.example.hutechdrugapp.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hutechdrugapp.DetailsActivity;
import com.example.hutechdrugapp.Model.Medicine;
import com.example.hutechdrugapp.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {

    private List<Medicine> medicineList;
    private static DecimalFormat df2 = new DecimalFormat("#.#");

    public DrugAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.setLocationData(medicineList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //    holder.cardview2.setCardBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryLight));
                    Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("MedicineObject",medicineList.get(position));
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
        return medicineList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView kbvLocation;
        private TextView textTitle, textLocation, textStarRating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            kbvLocation = itemView.findViewById(R.id.kbvLocations);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation = itemView.findViewById(R.id.textLocation);
            textStarRating = itemView.findViewById(R.id.textStarRating);
        }

        void setLocationData(Medicine medicine){
            Picasso.get().load(medicine.getHinhAnh()).into(kbvLocation);
            textTitle.setText(medicine.getTenThuoc());
            textLocation.setText(String.format("HSD: %s", medicine.getHSD()));
            double random_double = Math.random() * (5 - 4 + 0.1) + 4;
            textStarRating.setText(df2.format(random_double));
        }
    }
}


