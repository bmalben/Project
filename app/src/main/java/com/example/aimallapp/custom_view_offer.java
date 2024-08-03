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

public class custom_view_offer extends BaseAdapter {

    String[]of_id,price,dis,title;
    private  Context context;
    public custom_view_offer(Context applicationContext, String[] of_id, String[] price, String[] dis, String[] title) {
        this.context = applicationContext;
        this.of_id = of_id;
        this.price = price;
        this.dis = dis;
        this.title = title;
    }

    @Override
    public int getCount() {
        return price.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_offer,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView23);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView22);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView20);


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);


        tv1.setText(price[i]);
        tv2.setText(dis[i]);
        tv3.setText(title[i]);




        return gridView;
    }

}