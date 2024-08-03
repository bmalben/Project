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

import com.squareup.picasso.Picasso;

public class custom_view_products extends BaseAdapter {
    String[] product_id, name, price, image;
    private Context context;
    SharedPreferences sh;
    String url;


    public custom_view_products(Context applicationContext, String[] product_id, String[] name, String[] price, String[] image) {
        this.context = applicationContext;
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.image = image;

    }

    @Override
    public int getCount() {
        return name.length;
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
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_products, null);

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView37);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView39);
        ImageView im = (ImageView) gridView.findViewById(R.id.imageView2);
        Button b1 = (Button)gridView.findViewById(R.id.button11);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();
                sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("product_id",product_id[pos]);
                ed.commit();
                Intent i = new Intent(context,add_quantity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);


        tv1.setText(name[i]);
        tv2.setText(price[i]);


        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("ipaddress", "");

        String url = "http://" + ip + ":7000/" + image[i] + ".jpg";


        Picasso.with(context).load(url).transform(new CircleTransform()).into(im);

        return gridView;
    }
}