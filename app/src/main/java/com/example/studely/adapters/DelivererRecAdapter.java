package com.example.studely.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DelivererRecAdapter extends RecyclerView.Adapter<DelivererRecAdapter.DelivererViewHolder> {

    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DelivererViewHolder extends RecyclerView.ViewHolder {
        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
