package com.fub.smart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fub.smart.models.Item;
import com.fub.smart.utils.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class DisplayItemsActivity  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List itemsDataList =new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        recyclerView = findViewById(R.id.recyclerViewItemsList);
        itemAdapter=new ItemAdapter(itemsDataList);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(itemAdapter);
        ItemsDataPrepare();
    }

    void ItemsDataPrepare (){

        Item i1=new Item("1","1","1");
        Item i2=new Item("2","2","2");
        Item i3=new Item("3","3","3");
        itemsDataList.add(i1);
        itemsDataList.add(i2);
        itemsDataList.add(i3);
    }
}
