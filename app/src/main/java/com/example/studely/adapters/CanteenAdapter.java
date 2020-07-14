package com.example.studely.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studely.DeliverOrderSelect;
import com.example.studely.OrderCanteenSelect;
import com.example.studely.OrderConfirm;
import com.example.studely.OrderDelivererSelect;
import com.example.studely.OrderStallSelect;
import com.example.studely.R;
import com.example.studely.misc.QtyTextChanged;

import java.util.List;

public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {


    List<String> canteenList;
    List<Integer> postings;
    Context context;
    String address;
    Boolean order;

    public CanteenAdapter(Context context, List<String> canteenList, List<Integer> postings) {
        this.context = context;
        this.canteenList = canteenList;
        this.address = null;
        this.order = false;
        this.postings = postings;
    }

    public CanteenAdapter(Context context, List<String> canteenList, String address, List<Integer> postings) {
        this.context = context;
        this.canteenList = canteenList;
        this.address = address;
        this.order = true;
        this.postings = postings;
    }

    @NonNull
    @Override
    public CanteenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.canteen_row, parent, false);
        return new CanteenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CanteenViewHolder holder, final int position) {
        holder.canteenName.setText(canteenList.get(position));
        holder.postingsText.setText(Integer.toString(postings.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order) {
                    Intent newIntent = new Intent(context, OrderStallSelect.class);
                    newIntent.putExtra("canteenID", canteenList.get(position));
                    newIntent.putExtra("orderDestination", address);
                    context.startActivity(newIntent);
                } else {
                    Intent newIntent = new Intent(context, DeliverOrderSelect.class);
                    newIntent.putExtra("canteenID", canteenList.get(position));
                    context.startActivity(newIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return canteenList.size();
    }

    public class CanteenViewHolder extends RecyclerView.ViewHolder {

        TextView canteenName, postingsText;
        ConstraintLayout mainLayout;

        public CanteenViewHolder(@NonNull View itemView) {
            super(itemView);
            canteenName = itemView.findViewById(R.id.canteenName);
            postingsText = itemView.findViewById(R.id.noOfPostings);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
