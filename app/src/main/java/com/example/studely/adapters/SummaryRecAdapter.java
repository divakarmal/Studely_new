package com.example.studely.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.R;
import com.example.studely.classes.Food;

import java.util.List;

public class SummaryRecAdapter extends RecyclerView.Adapter<SummaryRecAdapter.SummaryViewHolder> {

    Context context;
    List<Food> itemList;

    public SummaryRecAdapter(Context context, List<Food> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.summary_row, parent, false);
        return new SummaryRecAdapter.SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        Food food = itemList.get(position);
        holder.itemText.setText(food.name);
        //holder.priceText.setText("$" + String.valueOf(food.price));
        holder.qtyText.setText(String.valueOf(food.quantity));
        holder.totalCost.setText("$" + food.calcCost());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder {

        TextView itemText, priceText, qtyText, totalCost;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.itemText);
            //priceText = itemView.findViewById(R.id.priceText);
            qtyText = itemView.findViewById(R.id.qtyText);
            totalCost = itemView.findViewById(R.id.totalCost);
        }
    }
}
