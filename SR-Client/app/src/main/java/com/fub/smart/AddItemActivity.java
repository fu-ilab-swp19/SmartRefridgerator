package com.fub.smart;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    TextView productId, productName, productDescription, productBrand, shelfNum;
    EditText expireDate;
    SeekBar shelfNumberSeekBar;


    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        String productIdValue = getIntent().getExtras().getString("scanResult");
        productId = findViewById(R.id.textViewItemId);
        productId.setText(productIdValue);
        productName = findViewById(R.id.textViewItemName);
        productDescription = findViewById(R.id.textViewItemDesc);
        productBrand = findViewById(R.id.textViewItemBrand);
        shelfNum = findViewById(R.id.textViewLabelShelfNumber);
        expireDate=findViewById(R.id.editTextExpiryDate);
        getProductDetails();


        shelfNumberSeekBar = findViewById(R.id.seekBarShelfNumber); // initiate the Seekbar
        shelfNumberSeekBar.setMax(2);
        shelfNumberSeekBar.setProgress(1);
        shelfNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
                shelfNum.setText((progress + 1) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    private void showDate(int year, int month, int day) {
        expireDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
     };


    public void getProductDetails() {

        String URL = com.fub.smart.utils.Environment.SERVER_URL + "product/" + productId.getText();
        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("result").equals("success")) {

                            productName.setText(response.getJSONObject("product").getString("name"));
                            productDescription.setText(response.getJSONObject("product").getString("description"));
                            productBrand.setText(response.getJSONObject("product").getString("brand"));
                        } else {
                            Toast.makeText(AddItemActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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
    private int getUserId(){
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getInt("userId", -1);

    }


    public void addItemInFridge(View v) {

        String URL = com.fub.smart.utils.Environment.SERVER_URL + "addproduct/mobile";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("productId", productId.getText());
            jsonBody.put("userId", getUserId());
            jsonBody.put("expirationDate", expireDate.getText());
            jsonBody.put("shelfNum", shelfNumberSeekBar.getProgress()+1);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if(response.getString("result").equals("success")) {
                            Toast.makeText(AddItemActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(myIntent);
                        }
                        else {
                            Toast.makeText(AddItemActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "defineProductRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
