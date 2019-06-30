package com.fub.smart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fub.smart.utils.Environment;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {


    CheckBox checkBoxNotifyProductOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        checkBoxNotifyProductOut = findViewById(R.id.checkBoxNotifyProductOut);
        checkBoxNotifyProductOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCheckBoxValue();
            }
        });
        getCheckBoxValue();

    }


    private void getCheckBoxValue() {
        String URL = Environment.SERVER_URL + "user/productOutNoti/" + getUserId();

        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("result").equals("success")) {

                            checkBoxNotifyProductOut.setChecked(response.getJSONObject("user").getInt("productOutNoti")!=0);
                        } else {
                            Toast.makeText(SettingsActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "getNotifiOutRequest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCheckBoxValue() {
        String URL = Environment.SERVER_URL + "user/productOutNoti/" + getUserId();

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("productOutNoti",checkBoxNotifyProductOut.isChecked()?1:0);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("result").equals("success")) {

                            //Toast.makeText(SettingsActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(SettingsActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "setNotifiOutRequest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getUserId() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getInt("userId", -1);

    }


}
