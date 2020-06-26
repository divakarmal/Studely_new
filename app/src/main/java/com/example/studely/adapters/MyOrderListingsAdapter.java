package com.example.studely.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studely.R;

import java.util.List;

public class MyOrderListingsAdapter extends RecyclerView.Adapter<MyOrderListingsAdapter.myOrderListingsView> {

    Context context;
    List<String> destinationList;
    List<String> timeList;

    public MyOrderListingsAdapter(Context context, List<String > timeList, List<String> destinationList) {
        this.timeList = timeList;
        this.destinationList = destinationList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrderListingsAdapter.myOrderListingsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_listitng_row, parent, false);
        return new MyOrderListingsAdapter.myOrderListingsView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderListingsAdapter.myOrderListingsView holder, int position) {
        String destination = destinationList.get(position);
        String time = timeList.get(position);
        holder.destText.setText(destination);
        holder.deliTimeText.setText(time);
    }

    @Override
    public int getItemCount() {
        return this.destinationList.size();
    }



    public class myOrderListingsView extends RecyclerView.ViewHolder{

        TextView destText, deliTimeText;
        ConstraintLayout mainLayout;

        public myOrderListingsView(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}