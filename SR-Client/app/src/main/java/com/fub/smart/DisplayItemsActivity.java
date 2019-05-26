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
    private int listType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        listType = getIntent().getExtras().getInt("listType");

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


        Item i1=new Item("1","Milch","JA-Milch","400","1","eine Flasche Milsch ohne Laktose","10/12/2019");
        Item i2=new Item("2","Fleish","Green Farm","800","2","Hackfleisch aus Schwein","10/12/2019");
        Item i3=new Item("3","Wasser","Veo","300","3","1/5 Liter still Wasser","10/12/2019");
        Item i4=new Item("1","Milch","JA-Milch","400","1","eine Flasche Milsch ohne Laktose","10/12/2019");
        Item i5=new Item("2","Fleish","Green Farm","800","2","Hackfleisch aus Schwein","10/12/2019");
        Item i6=new Item("3","Wasser","Veo","300","3","1/5 Liter still Wasser","10/12/2019");
        Item i7=new Item("3","Wasser","Veo","300","3","1/5 Liter still Wasser","10/12/2019");
        if(listType==1){
            itemsDataList.add(i1);
            itemsDataList.add(i2);
            itemsDataList.add(i3);
            itemsDataList.add(i4);
            itemsDataList.add(i5);
            itemsDataList.add(i6);
            itemsDataList.add(i7);

        }
        else if(listType==2)
        {
            itemsDataList.add(i1);
            itemsDataList.add(i2);
        }else{

            itemsDataList.add(i3);
        }

    }
}
