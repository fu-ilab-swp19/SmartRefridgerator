package com.fub.smart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView allProducts,outProducts,expiredPRoducts,buyListProducts;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        allProducts=findViewById(R.id.textViewDashboardAllItems);
        outProducts=findViewById(R.id.textViewDashboardOutStock);
        expiredPRoducts=findViewById(R.id.textViewDashboardNearExpiration);
        buyListProducts=findViewById(R.id.textViewDashboardBuylist);
        updateToken();
        getStatistics();

    }
    private void updateToken(){
        String serviceUrl =   serviceUrl = "user/token/" + getUserId();



        String URL = com.fub.smart.utils.Environment.SERVER_URL + serviceUrl;
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("token",getToken());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("token","token updated");
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

    private void getStatistics(){
        String serviceUrl =   serviceUrl = "user/statistics/" + getUserId();



        String URL = com.fub.smart.utils.Environment.SERVER_URL + serviceUrl;
        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                       allProducts.setText(response.getString("all"));
                       outProducts.setText(response.getString("out"));
                       expiredPRoducts.setText(response.getString("expired"));
                       buyListProducts.setText(response.getString("buylist"));
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(getBaseContext(),   SettingsActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_add_product) {

            Intent myIntent = new Intent(getBaseContext(),   ScannerActivity.class);
            myIntent.putExtra("scanGoal",1);
            startActivity(myIntent);
        }else if (id == R.id.nav_add_item_fridge) {

            Intent myIntent = new Intent(getBaseContext(),   ChooseItemTypeActivity.class);
            startActivity(myIntent);

        }else if (id == R.id.nav_display_all) {
            Intent myIntent = new Intent(getBaseContext(),   DisplayItemsActivity.class);
            myIntent.putExtra("listType",1);
            startActivity(myIntent);

        } else if (id == R.id.nav_items_expiration) {
            Intent myIntent = new Intent(getBaseContext(),   DisplayItemsActivity.class);
            myIntent.putExtra("listType",2);
            startActivity(myIntent);


        } else if (id == R.id.nav_items_out_stock) {
            Intent myIntent = new Intent(getBaseContext(),   DisplayItemsActivity.class);
            myIntent.putExtra("listType",3);
            startActivity(myIntent);

        }else if (id == R.id.nav_buy_list) {
            Intent myIntent = new Intent(getBaseContext(),   BuyListActivity.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private int getUserId() {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getInt("userId", -1);

    }

    private String getToken(){
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getString("token", "");

    }




}
