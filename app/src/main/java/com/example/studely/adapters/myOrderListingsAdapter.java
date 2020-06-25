package com.example.studely.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studely.R;

import java.util.List;

public class myOrderListingsAdapter extends RecyclerView.Adapter<myOrderListingsAdapter.myOrderListingsView> {

    Context context;
    List<String> destinationList;
    List<String> timeList;

    public myOrderListingsAdapter(Context context, List<String > timeList, List<String> destinationList) {
        this.timeList = timeList;
        this.destinationList = destinationList;
        this.context = context;
    }

    @NonNull
    @Override
    public myOrderListingsAdapter.myOrderListingsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_listitng_row, parent, false);
        return new myOrderListingsAdapter.myOrderListingsView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myOrderListingsAdapter.myOrderListingsView holder, int position) {
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

        public myOrderListingsView(@NonNull View itemView) {
            super(itemView);
            destText = itemView.findViewById(R.id.destText);
            deliTimeText = itemView.findViewById(R.id.deliTimeText);
        }
    }
}
