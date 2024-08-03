package com.example.aimallapp;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import android.speech.tts.TextToSpeech;
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


public class macservice extends Service {
	String url="";
	String mac_list="", wifi_namelist="";
	BroadcastReceiver receiver;
    List<ScanResult> wifiList;
    ArrayList<String> mac_id=new ArrayList<String>();
    ArrayList<String> wifiname=new ArrayList<String>();
    TextToSpeech textToSpeech;

    ArrayList<Integer> level_value=new ArrayList<Integer>();
    WifiManager wifi;
 	Handler hd,hd1;
	
	public void test()
	{
		hd=new Handler();
		hd.post(r);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		test();

		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hd.removeCallbacks(r);
	}
	//	private void getWifi() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if (ActivityCompat.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0x12345);
//            } else {
//               // doGetWifi(); // the actual wifi scanning
//            }
//		}
//	}

Runnable r=new Runnable() {
	@Override
	public void run() {

	//	Toast.makeText(getApplicationContext(),"In tread",Toast.LENGTH_SHORT).show();


		mac_id=new ArrayList<String>();
		 wifiname=new ArrayList<String>();
		    
		  
	     level_value=new ArrayList<Integer>();
		
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifi.getWifiState();

	Toast.makeText(getApplicationContext(),"In wifi state"+state,Toast.LENGTH_SHORT).show();

		SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String mac_list= sh.getString("mac_list","");
		Toast.makeText(macservice.this, "mac"+mac_list, Toast.LENGTH_SHORT).show();





		if(state == WifiManager.WIFI_STATE_ENABLED) {

//			Toast.makeText(getApplicationContext(),"In wifi check in ",Toast.LENGTH_SHORT).show();


			setupWifiScanner();








			wifiList = wifi.getScanResults();
            for (int i = 0; i < wifiList.size(); i++){
            	mac_id.add((wifiList.get(i)).BSSID); 
            	wifiname.add((wifiList.get(i)).SSID);
                ScanResult result=wifiList.get(i);
                int level=result.level;
                level_value.add(level);
            }

		Toast.makeText(getApplicationContext(),"no of wifi points"+wifiList.size(),Toast.LENGTH_SHORT).show();


			search_results();


        }
       
       hd.postDelayed(r, 600000);
	}
};


void setupWifiScanner() {

    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    final IntentFilter filter = new IntentFilter();
    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    registerReceiver(new BroadcastReceiver(){
        public void onReceive(Context c, Intent i){
            //scanResultHandler(wifiManager.getScanResults());
            wifiManager.startScan();
        }
		
    }, filter);
    wifiManager.startScan();
}
public void search_results(){
	
	if(mac_id.size()>0){
		
		mac_list="";
		//Toast.makeText(getApplicationContext(),mac_id.size()+"",Toast.LENGTH_LONG).show();
		for(int i=0;i<mac_id.size();i++){
			
			for(int j=i+1;j<mac_id.size();j++){
				
				if(level_value.get(i)<level_value.get(j)){
					
					int k=level_value.get(i);
					level_value.set(i, level_value.get(j));
					level_value.set(j, k);
					
					String temp_mac=mac_id.get(i);
					mac_id.set(i, mac_id.get(j));
					mac_id.set(j, temp_mac);
					
					temp_mac=wifiname.get(i);
					wifiname.set(i, wifiname.get(j));
					wifiname.set(j, temp_mac);

					
				}
				
			}
			mac_list+=mac_id.get(i)+"#";
			wifi_namelist+=wifiname.get(i)+"#";
	}

		SharedPreferences ss= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor ed=ss.edit();
		ed.putString("mac_list",mac_list);
		ed.putString("wifi_namelist",wifi_namelist);
		ed.commit();



	}
	}




}


