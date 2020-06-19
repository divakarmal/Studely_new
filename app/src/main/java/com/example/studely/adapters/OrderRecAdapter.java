package com.example.studely.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRecAdapter extends RecyclerView.Adapter<OrderRecAdapter.OrderViewHolder> {

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
