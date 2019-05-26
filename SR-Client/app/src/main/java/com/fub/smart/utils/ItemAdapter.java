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
        myViewHolder.amount.setText(data.getItemAmount());
        myViewHolder.shelf.setText(String.valueOf(data.getItemShelf()));
        myViewHolder.desc.setText(data.getItemDesc());
        myViewHolder.expireDate.setText(data.getItemExpireDate());

    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,brand,amount,shelf,expireDate,desc;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.itemName);
            brand=itemView.findViewById(R.id.itemBrand);
            amount=itemView.findViewById(R.id.itemAmount);
            shelf=itemView.findViewById(R.id.itemShelf);
            expireDate= itemView.findViewById(R.id.itemExpireDate);
            desc=itemView.findViewById(R.id.itemDesc);
        }
    }
}
