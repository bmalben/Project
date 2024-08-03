package com.example.aimallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ip_page extends AppCompatActivity {
    EditText e1;
    Button b1;
    SharedPreferences sh;
    testres settingsContentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_page);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1 = findViewById(R.id.editTextTextPersonName);
        e1.setText(sh.getString("ipaddress",""));
        b1 = findViewById(R.id.button);
        settingsContentObserver = new testres(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.
                CONTENT_URI, true, settingsContentObserver);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipaddress = e1.getText().toString();
                int flag=0;
                if(ipaddress.equalsIgnoreCase("")){
                    e1.setError("Null");
                    flag++;
                }
                if(flag==0) {


                    String url = "http://" + ipaddress + ":7000/";
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("ipaddress", ipaddress);
                    ed.putString("url", url);
                    ed.commit();
                    startService(new Intent(getApplicationContext(),macservice.class));
                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);


                }
            }
        });

    }
}