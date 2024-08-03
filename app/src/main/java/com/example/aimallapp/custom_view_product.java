package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_view_product extends BaseAdapter {

    SharedPreferences sh;
    String url;

    String[]p_id,pname,price,img,shopname,quantity,total;
    private  Context context;
    public custom_view_product(Context applicationContext, String[] p_id, String[] pname, String[] price, String[] img, String[] shopname, String[] quantity, String[] total) {
        this.context = applicationContext;
        this.p_id = p_id;
        this.pname = pname;
        this.price = price;
        this.img = img;
        this.shopname = shopname;
        this.quantity = quantity;
        this.total = total;
    }

    @Override
    public int getCount() {
        return pname.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_product,null);

        }
        else
        {
            gridView=(View)view;

        }

        ImageView im=(ImageView)gridView.findViewById(R.id.imageView3);
        TextView tv1=(TextView)gridView.findViewById(R.id.textView18);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView29);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView43);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView41);
        TextView tv5=(TextView)gridView.findViewById(R.id.textView15);
        Button b1=(Button)gridView.findViewById(R.id.button5);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();

                sh = PreferenceManager.getDefaultSharedPreferences(context);
                sh.getString("ipaddress", "");
                url = sh.getString("url", "") + "and_cart_cancel";


                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                // response
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(context, "Cart deleted", Toast.LENGTH_SHORT).show();
                                        Intent ij = new Intent(context, view_product.class);
                                        ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(ij);
                                    }



                                    // }
                                    else {
                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("p_id",p_id[pos]);


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
        });



        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(pname[i]);
        tv2.setText(price[i]);
        tv3.setText(quantity[i]);
        tv4.setText(shopname[i]);
        tv5.setText(total[i]);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sh.getString("ipaddress","");

        String url="http://" + ip + ":7000"+img[i];


        Picasso.with(context).load(url). into(im);




        return gridView;
    }
}