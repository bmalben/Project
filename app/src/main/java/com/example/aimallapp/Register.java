package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    RadioGroup r1;
    RadioButton rb1,rb2;

    Button b1;
    String gender="male";
    String url;
    SharedPreferences sh;
    String password_pattern = "[A-Za-z0-9]{3,9}";
    String pin_pattern = "[0-9]{6}";
    String ph_pattern = "[0-9]{10}";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e1 = findViewById(R.id.editTextTextPersonName4);
        e2 = findViewById(R.id.editTextPhone);
        e3 = findViewById(R.id.editTextTextPersonName5);
        e4 = findViewById(R.id.editTextNumber);
        e5 = findViewById(R.id.editTextTextPersonName6);
        e6 = findViewById(R.id.editTextTextEmailAddress);
        e7 = findViewById(R.id.editTextTextPassword2);
        e8 = findViewById(R.id.editTextTextPassword3);
        r1 = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        b1 = findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb2.isChecked()){
                    gender="female";
                }
                String name = e1.getText().toString();
                String phone = e2.getText().toString();
                String place = e3.getText().toString();
                String pin = e4.getText().toString();
                String post = e5.getText().toString();
                String email = e6.getText().toString();
                String password = e7.getText().toString();
                String con_password = e8.getText().toString();

                int flag=0;
                if(name.equalsIgnoreCase("")){
                    e1.setError("Null");
                    flag++;
                }
                if(!phone.matches(ph_pattern)){
                    e2.setError("Null");
                    flag++;
                }
                if(place.equalsIgnoreCase("")){
                    e3.setError("Null");
                    flag++;
                }
                if(!pin.matches(pin_pattern)){
                    e4.setError("Null");
                    flag++;
                }
                if(post.equalsIgnoreCase("")){
                    e5.setError("Null");
                    flag++;
                }
                if(email.equalsIgnoreCase("")){
                    e6.setError("Null");
                    flag++;
                }
                if(!password.matches(password_pattern)){
                    e7.setError("Null");
                    flag++;
                }
                if(con_password.equalsIgnoreCase("")){
                    e8.setError("Null");
                    flag++;
                }


                if(flag==0) {
                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ipaddress", "");
                    url = sh.getString("url", "") + "and_Register";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    // response
                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                            Toast.makeText(Register.this, "Done", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), login.class);
                                            startActivity(i);

                                        }


                                        // }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("name", name);
                            params.put("phone", phone);
                            params.put("place", place);
                            params.put("pin", pin);
                            params.put("post", post);
                            params.put("email", email);
                            params.put("gender", gender);
                            params.put("password", password);


                            return params;
                        }
                    };

                    int MY_SOCKET_TIMEOUT_MS = 100000;

                    postRequest.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(postRequest);


                }
            }
        });
    }
}