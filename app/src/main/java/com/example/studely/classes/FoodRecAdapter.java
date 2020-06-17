package com.example.studely.classes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    public int[] qty;
    Context context;
    private QtyTextChanged qtyTextChanged;

    public FoodRecAdapter(Context context, String[] foodItems, QtyTextChanged qtyTextChanged) {
        this.context = context;
        this.foodItems = foodItems;
        this.qty = new int[foodItems.length];
        this.qtyTextChanged = qtyTextChanged;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_item_row, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, final int position) {
        holder.foodItemText.setText(foodItems[position]);
        EditText qtyNum = holder.qtyNum;
        qtyNum.setText(String.valueOf(qty[position]));

        qtyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qtyTextChanged.OnTextChanged(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
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
