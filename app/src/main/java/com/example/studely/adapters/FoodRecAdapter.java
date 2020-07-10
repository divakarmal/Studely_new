package com.example.studely.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.R;
import com.example.studely.misc.QtyTextChanged;

public class FoodRecAdapter extends RecyclerView.Adapter<FoodRecAdapter.FoodViewHolder> {

    public int[] qty;
    String[] foodItems;
    Integer[] prices;
    Context context;
    private QtyTextChanged qtyTextChanged;

    public FoodRecAdapter(Context context, String[] foodItems, QtyTextChanged qtyTextChanged, Integer[] prices) {
        this.context = context;
        this.foodItems = foodItems;
        this.prices = prices;
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
        holder.price.setText(String.valueOf(prices[position]));
        EditText qtyNum = holder.qtyNum;
        qtyNum.setText(String.valueOf(qty[position]));

        qtyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qtyTextChanged.OnTextChanged(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.length;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView foodItemText;
        TextView price;
        EditText qtyNum;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodItemText = itemView.findViewById(R.id.foodItemText);
            qtyNum = itemView.findViewById(R.id.qtyNum);
            price = itemView.findViewById(R.id.priceText);
        }
    }
}
