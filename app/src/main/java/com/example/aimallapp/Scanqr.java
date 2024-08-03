package com.example.aimallapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Scanqr extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btscan;

    ListView lvs;
    String[] id,name;
    SharedPreferences sh;
    String ip, url, lid;
    String[] na, fid,dir;
    TextToSpeech textToSpeech;
    String contents="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });


        btscan=(Button) findViewById(R.id.button4);
        btscan.setOnClickListener(this);

        btscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanQR(v);


            }
        });


        lvs=(ListView) findViewById(R.id.lvs);


        lvs.setOnItemClickListener(this);


    }

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public void scanQR(View v) {
        try {

            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(Scanqr.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
//=====================================================


//                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                String ip=sp.getString("url","");
//                String url= ip+"/and_getmessage";
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ipaddress", "");
                url = sh.getString("url", "") + "and_getmessage";


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
                                        Toast.makeText(getApplicationContext(), "checkin", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), Home.class);
                                        startActivity(i);

                                    }
                                    if (jsonObj.getString("status").equalsIgnoreCase("okk")) {
                                        Toast.makeText(getApplicationContext(), "checkout marked", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), Home.class);
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

//                        params.put("name", name);
//                        params.put("phone", phone);
//                        params.put("place", place);
//                        params.put("pin", pin);
//                        params.put("post", post);
//                        params.put("email", email);
//                        params.put("gender", gender);
                        params.put("content", contents);
                        params.put("lid", sh.getString("lid",""));


                        return params;
                    }
                };

                int MY_SOCKET_TIMEOUT_MS = 100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);

//                / Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();



            }
        }





    }

    @Override
    public void onClick(View v) {






    }

    ProgressDialog pd;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final String todestination= fid[position];
        final String strtid= contents;


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String apiURL="http://" + sh.getString("ip","") + ":5000/getpaths";


        pd = new ProgressDialog(Scanqr.this);
        pd.setMessage("Uploading....");
        pd.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, apiURL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(new String(response.data));
                            String dis = obj.getString("status");
                            if (dis.equalsIgnoreCase("ok")) {
                                String dir = obj.getString("direction");

//                                dir.setText(jj.getString("direction"));

                                textToSpeech.speak(""+dir,TextToSpeech.QUEUE_FLUSH,null);



                                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor ed=sh.edit();
                                ed.putString("res",new String(response.data));
                                ed.commit();

                                Intent ins= new Intent(getApplicationContext(),Scanqr.class);
                                startActivity(ins);




                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to route", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String voice = sh.getString("voice", "");
                params.put("voice", voice);
                params.put("strtid",contents);
                params.put("fid", todestination);
//                params.put("voice",vo)


                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);






    }
}
