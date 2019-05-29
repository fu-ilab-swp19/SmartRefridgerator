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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fub.smart.utils.Environment;
import com.fub.smart.utils.SmartRefrigerator;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName,lastName,password,rPassword,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName=findViewById(R.id.editTextSignUpFirstName);
        lastName=findViewById(R.id.editTextSignUpLAstName);
        password=findViewById(R.id.editTextSignUpPassword);
        rPassword=findViewById(R.id.editTextSignUpRepeatPassword);
        email=findViewById(R.id.editTextSignUpUserName);
        getToken();

    }

    private String getToken(){
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return saved_values.getString("token", "");

    }


    public void registerUser(View v) {


        String URL = Environment.SERVER_URL + "user";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", firstName.getText());
            jsonBody.put("lastName", lastName.getText());
            jsonBody.put("email", email.getText());
            jsonBody.put("password", password.getText());
            jsonBody.put("token", getToken());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if(response.getString("result").equals("success")) {
                            Intent myIntent = new Intent(getBaseContext(), SignInActivity.class);
                            startActivity(myIntent);
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

            //RequestQueue requestQueue = Volley.newRequestQueue(this);
            //requestQueue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
