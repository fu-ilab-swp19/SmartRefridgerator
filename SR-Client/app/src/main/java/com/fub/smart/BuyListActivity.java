package com.fub.smart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fub.smart.models.BuyListItem;
import com.fub.smart.utils.BuyListItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class BuyListActivity  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BuyListItemAdapter itemAdapter;
    private List itemsDataList =new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list);
        recyclerView = findViewById(R.id.recyclerViewBuyList);
        itemAdapter=new BuyListItemAdapter(itemsDataList);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(itemAdapter);
        ItemsDataPrepare();
    }

    void ItemsDataPrepare (){

        BuyListItem i1=new BuyListItem("1","Milch","10/11/2019","500");
        BuyListItem i2=new BuyListItem("2","Wasser","10/6/2019","700");
        BuyListItem i3=new BuyListItem("3","Appfel","9/11/2019","800");
        itemsDataList.add(i1);
        itemsDataList.add(i2);
        itemsDataList.add(i3);
    }
}
