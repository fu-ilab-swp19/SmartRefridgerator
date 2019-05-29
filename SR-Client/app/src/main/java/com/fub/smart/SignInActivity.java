package com.fub.smart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

public class SignInActivity  extends AppCompatActivity {

    EditText email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        email=findViewById(R.id.editTextSignInUserName);
        password=findViewById(R.id.editTextSignINPassword);
    }

    public void signin(View v){

        String URL = Environment.SERVER_URL + "user/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email.getText());
            jsonBody.put("password", password.getText());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if(response.getString("result").equals("success")) {

                            SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor=saved_values.edit();
                            editor.putInt("userId",response.getJSONObject("user").getInt("id")).commit();
                            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(myIntent);
                        }
                        else {
                            Toast.makeText(SignInActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

            SmartRefrigerator.getInstance().addToRequestQueue(jsonObjReq, "signInRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void createAccount(View v){
        Intent intent = new Intent(getApplicationContext(),
                SignUpActivity.class);
        startActivity(intent);

    }
}
