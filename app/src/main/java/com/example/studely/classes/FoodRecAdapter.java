package com.example.studely.classes;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.R;

public class FoodRecAdapter extends RecyclerView.Adapter<FoodRecAdapter.FoodViewHolder> {

    String[] foodItems;
    int[] qty;
    Context context;

    public FoodRecAdapter(Context context, String[] foodItems) {
        this.context = context;
        this.foodItems = foodItems;
        this.qty = new int[foodItems.length];
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_item_row, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodItemText.setText(foodItems[position]);
        holder.qtyNum.setText(String.valueOf(qty[position]));
    }

    @Override
    public int getItemCount() {
        return foodItems.length;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView foodItemText;
        EditText qtyNum;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodItemText = itemView.findViewById(R.id.foodItemText);
            qtyNum = itemView.findViewById(R.id.qtyNum);
        }
    }
}
