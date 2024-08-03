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
import android.widget.TextView;

public class custom_view_shop extends BaseAdapter {
    SharedPreferences sh;
    String url;

    String[]sh_id,floor_info,sname,stype,owner,email,phno;
    private  Context context;
    public custom_view_shop(Context applicationContext, String[] sh_id, String[] floor_info, String[] sname, String[] stype, String[] owner, String[] email, String[] phno) {
        this.context = applicationContext;
        this.sh_id = sh_id;
        this.floor_info = floor_info;
        this.sname = sname;
        this.stype = stype;
        this.owner = owner;
        this.email = email;
        this.phno = phno;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_shop,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView7);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView9);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView10);
        TextView tv5=(TextView)gridView.findViewById(R.id.textView11);
        TextView tv6=(TextView)gridView.findViewById(R.id.textView12);
        Button b1=(Button)gridView.findViewById(R.id.button4);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (int)view.getTag();
                sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("sh_id",sh_id[pos]);
                ed.commit();
                Intent i = new Intent(context,view_products.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);


        tv1.setText(floor_info[i]);
        tv2.setText(sname[i]);
        tv3.setText(stype[i]);
        tv4.setText(owner[i]);
        tv5.setText(email[i]);
        tv6.setText(phno[i]);




        return gridView;
    }
}