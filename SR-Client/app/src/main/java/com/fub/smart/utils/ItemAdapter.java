package com.fub.smart.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fub.smart.R;
import com.fub.smart.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter  {

    List itemDataList;
    public ItemAdapter(List itemDataList) {
        this.itemDataList=itemDataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        MyViewHolder myViewHolder=(MyViewHolder)viewHolder;
        Item data=(Item)itemDataList.get(i);
        myViewHolder.name.setText(data.getItemName());
        myViewHolder.brand.setText(String.valueOf(data.getItemBrand()));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,brand;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            brand=itemView.findViewById(R.id.brand);
        }
    }
}
