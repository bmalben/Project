package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_view_notifications extends BaseAdapter {

    String[]n_id,noti,date;
    private  Context context;
    public custom_view_notifications(Context applicationContext, String[] n_id, String[] noti, String[] date) {
        this.context = applicationContext;
        this.n_id = n_id;
        this.noti = noti;
        this.date = date;
    }

    @Override
    public int getCount() {

        return noti.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_notifications,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView28);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView26);


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);


        tv1.setText(noti[i]);
        tv2.setText(date[i]);




        return gridView;
    }
}
