package com.fub.smart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fub.smart.models.Item;
import com.fub.smart.utils.ItemAdapter;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List itemsDataList = new ArrayList<>();
    private int listType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        listType = getIntent().getExtras().getInt("listType");

        recyclerView = findViewById(R.id.recyclerViewItemsList);
        itemAdapter = new ItemAdapter(itemsDataList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(itemAdapter);
        ItemsDataPrepare();
    }

    void ItemsDataPrepare() {


        String serviceUrl = "";

        if (listType == 1) {
            serviceUrl = "myproducts/" + getUserId();

        } else if (listType == 2) {
            serviceUrl = "myproducts/expire/" + getUserId();
        } else {

            serviceUrl = "myproducts/outstock/" + getUserId();

        }


        String URL = com.fub.smart.utils.Environment.SERVER_URL + serviceUrl;
        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("result").equals("success")) {

                            JSONArray products = response.getJSONArray("products");
                            fillDataInList(products);
                        } else {
                            Toast.makeText(DisplayItemsActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "getProductRequest");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getUserId() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getInt("userId", -1);

    }

    private void fillDataInList(JSONArray products) {

        for (int i = 0; i < products.length(); i++) {
            try {
                Item item = new Item(products.getJSONObject(i).getInt("id") + "",
                        products.getJSONObject(i).getJSONObject("Product").getString("name"),
                        products.getJSONObject(i).getJSONObject("Product").getString("brand"),
                        products.getJSONObject(i).getInt("amount") + "",
                        products.getJSONObject(i).getInt("shelfNum") + "",
                        products.getJSONObject(i).getJSONObject("Product").getString("description"),
                        products.getJSONObject(i).getString("expirationDate"));
                itemsDataList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        itemAdapter.notifyDataSetChanged();

    }
}
