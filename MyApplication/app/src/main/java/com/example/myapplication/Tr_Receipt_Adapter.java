package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Tr_Receipt_Adapter extends RecyclerView.Adapter<Tr_Receipt_Adapter.ViewHolder>{

    private List<Tr_Receipt> Tr_Rec_List = new ArrayList<>();

    private Context context;

    public Tr_Receipt_Adapter(Context context, ArrayList<Tr_Receipt> Tr_Rec_List){
        this.context = context;
        this.Tr_Rec_List = Tr_Rec_List;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView supplierTextView;
        TextView dateTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tr_receipt_name_et);
            supplierTextView = itemView.findViewById(R.id.tr_receipt_supplier);
            dateTextView = itemView.findViewById(R.id.tr_receipt_date);
            imageView = itemView.findViewById(R.id.tr_receipt_imgv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tr_list_receipt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(Tr_Rec_List.get(position).getTr_rec_url()).into(holder.imageView);
        holder.nameTextView.setText(Tr_Rec_List.get(position).getTr_rec_name());
        holder.supplierTextView.setText(Tr_Rec_List.get(position).getTr_rec_supplier());
        holder.dateTextView.setText(Tr_Rec_List.get(position).getTr_rec_date());

    }

    @Override
    public int getItemCount() {
        return Tr_Rec_List.size();
    }

    public void addData(Tr_Receipt newData) {
        Tr_Rec_List.add(newData);
        notifyDataSetChanged(); // Notify RecyclerView about the data change
    }

    public void sort(){
        Tr_Rec_List.sort(new RecComparator());
    }
}
