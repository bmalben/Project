package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class custom_view_service extends BaseAdapter {
    SharedPreferences sh;
    String url;

    String[]s_id,sname,lati,longi;
    private  Context context;

    public custom_view_service(Context applicationContext, String[] s_id, String[] sname, String[] lati, String[] longi) {
        this.context = applicationContext;
        this.s_id = s_id;
        this.sname = sname;
        this.lati = lati;
        this.longi = longi;
    }

    @Override
    public int getCount() {
        return sname.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_service,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView35);
        Button b1=(Button)gridView.findViewById(R.id.button7);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String strUri = "http://maps.google.com/maps?q=loc:" + lati[i] + "," + longi[i];
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });

        tv1.setTextColor(Color.BLACK);

        tv1.setText(sname[i]);




        return gridView;
    }
}
