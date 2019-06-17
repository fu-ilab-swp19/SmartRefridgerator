package com.fub.smart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fub.smart.models.BuyListItem;
import com.fub.smart.models.Item;
import com.fub.smart.utils.BuyListItemAdapter;
import com.fub.smart.utils.Environment;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



        String serviceUrl = "buylist/" + getUserId();



        String URL = com.fub.smart.utils.Environment.SERVER_URL + serviceUrl;
        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("result").equals("success")) {

                            JSONArray products = response.getJSONArray("items");
                            fillDataInList(products);
                        } else {
                            Toast.makeText(BuyListActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

    private void fillDataInList(JSONArray products) {
        itemsDataList.clear();
        for (int i = 0; i < products.length(); i++) {
            try {
                BuyListItem item = new BuyListItem(
                        products.getJSONObject(i).getJSONObject("Product").getLong("id") + "",
                        products.getJSONObject(i).getJSONObject("Product").getString("name"),
                        products.getJSONObject(i).getString("createdAt")
                       );
                itemsDataList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        itemAdapter.notifyDataSetChanged();

    }


    private int getUserId() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getInt("userId", -1);

    }


    public void removeItemFromBuyList(View v){

        String URL = Environment.SERVER_URL + "buylist/delete";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("productId", v.getTag());
            jsonBody.put("userId", getUserId());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if(response.getString("result").equals("success")) {

                            Toast.makeText(BuyListActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                            ItemsDataPrepare();
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

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "signUpRequest");

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

}
