package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_product extends AppCompatActivity {
    ListView li;
    String url;
    SharedPreferences sh;
    String[]p_id,pname,price,img,shopname,quantity,total;
    TextView t1,t2;
    Button b1;
ImageView im;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        li = findViewById(R.id.listview2);
        t1 = findViewById(R.id.textView31);
        t2 = findViewById(R.id.textView33);
        b1 = findViewById(R.id.button9);
        im = findViewById(R.id.imageView5);

        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
        im.setVisibility(View.INVISIBLE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),pay_mode.class);
                startActivity(i);
            }
        });



        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress","");
        url=sh.getString("url","")+ "and_view_cart" ;

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



                                t1.setVisibility(View.VISIBLE);
                                t2.setVisibility(View.VISIBLE);
                                b1.setVisibility(View.VISIBLE);

                                String gtotal= jsonObj.getString("sum");
                                t2.setText("₹ "+gtotal+" /-");
                                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor ed = sh.edit();
                                ed.putString("am", gtotal);
                                ed.commit();

                                JSONArray js= jsonObj.getJSONArray("data");
                                p_id=new String[js.length()];
                                pname=new String[js.length()];
                                price=new String[js.length()];
                                img=new String[js.length()];
                                shopname=new String[js.length()];
                                quantity=new String[js.length()];
                                total=new String[js.length()];
                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    p_id[i]=u.getString("p_id");
                                    pname[i]=u.getString("pname");
                                    price[i]=u.getString("price");
                                    img[i]=u.getString("img");
                                    shopname[i]=u.getString("shopname");
                                    quantity[i]=u.getString("quantity");
                                    total[i]=u.getString("total");



                                }
//
                                li.setAdapter(new custom_view_product(getApplicationContext(),p_id,pname,price,img,shopname,quantity,total));
                            }


                            // }
                            else {
                                im.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
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

                params.put("lid",sh.getString("lid",""));

                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Home.class);
        startActivity(i);
    }
}