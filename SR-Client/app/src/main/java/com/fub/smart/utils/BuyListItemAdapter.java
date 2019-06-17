package com.fub.smart.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fub.smart.R;
import com.fub.smart.models.BuyListItem;

import java.util.List;

public class BuyListItemAdapter extends RecyclerView.Adapter  {


    List buyListItemDataList;
    public BuyListItemAdapter(List buyListItemDataList) {
        this.buyListItemDataList=buyListItemDataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.buy_list_row, viewGroup, false);
        return new BuyListItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        BuyListItemAdapter.MyViewHolder myViewHolder=(BuyListItemAdapter.MyViewHolder)viewHolder;
        BuyListItem data=(BuyListItem)buyListItemDataList.get(i);
        myViewHolder.name.setText(data.getItemName());
        myViewHolder.reminderDate.setText(String.valueOf(data.getItemReminderDate()));
        myViewHolder.imageViewDeleteItem.setTag(data.getItemId());

    }

    @Override
    public int getItemCount() {
        return buyListItemDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,reminderDate;
        ImageView imageViewDeleteItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.buyListItemName);
            reminderDate=itemView.findViewById(R.id.buyListItemRemindereDate);
            imageViewDeleteItem=itemView.findViewById(R.id.imageViewDeleteItem);

        }
    }
}
