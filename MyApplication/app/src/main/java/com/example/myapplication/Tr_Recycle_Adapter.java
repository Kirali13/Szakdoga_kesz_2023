package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Tr_Recycle_Adapter extends RecyclerView.Adapter<Tr_Recycle_Adapter.ViewHolder> {
    private static final String LOG_TAG = MainActivity.class.getName();

    private List<Tr_Object> TrList = new ArrayList<>(); // Assuming Tr_Object is your object
    DocumentReference documentReference;

    private Context context;

    public Tr_Recycle_Adapter(Context ctx)
    {
        this.context = ctx;
    }

    // Other necessary fields and methods for RecyclerView.Adapter

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        TextView currencyTextView;
        TextView typeTextView;
        TextView groupTextView;
        TextView dateTextView;
        TextView tr_option;
        // Other TextViews for additional fields

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            groupTextView = itemView.findViewById(R.id.groupTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tr_option = itemView.findViewById(R.id.tr_option);
            // Find other TextViews here
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tr_Object object = TrList.get(position);

        ViewHolder vh = (ViewHolder) holder;

        holder.nameTextView.setText(object.getTr_name());
        holder.amountTextView.setText(String.valueOf(object.getTr_amount()));
        holder.currencyTextView.setText(String.valueOf(object.getCurrency()));
        holder.typeTextView.setText(String.valueOf(object.getType()));
        holder.groupTextView.setText(String.valueOf(object.getGroup()));
        holder.dateTextView.setText(String.valueOf(object.getTr_date()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        vh.tr_option.setOnClickListener(h->{
            PopupMenu popupMenu = new PopupMenu(context, vh.tr_option);
            popupMenu.inflate(R.menu.tr_ud_options);
            popupMenu.setOnMenuItemClickListener(i->{
                if (i.getItemId() == R.id.menu_update){
                    Intent intent = new Intent(context,Tr_Update.class);
                    intent.putExtra("SZERKESZTÉS",object);
                    context.startActivity(intent);
                } else if (i.getItemId() == R.id.menu_delete) {
                    String key = object.getKey();
                    DocumentReference documentReference;
                    documentReference = db.collection("tr10").document(key);
                    documentReference.delete().addOnSuccessListener(suc ->
                    {
                        Toast.makeText(context, "Record is removed", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                        TrList.remove(object);
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return TrList.size();
    }

    public void addData(Tr_Object newData) {
        TrList.add(newData);
        notifyDataSetChanged(); // Notify RecyclerView about the data change
    }
    public void deleteData(){
        TrList.clear();
    }

    public void sort(){
        TrList.sort(new DateComparator());
    }
}
