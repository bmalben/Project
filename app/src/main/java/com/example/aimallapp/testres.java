package com.example.aimallapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class testres extends ContentObserver {
    int previousVolume;
    Context context;

    TextToSpeech textToSpeech;




    testres(Context c, Handler handler) {
        super(handler);
        context = c;

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



        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume =
                Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume =
                Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC);
        int delta = previousVolume - currentVolume;
        if (delta > 0) {
            Toast.makeText(context, "Volume Decreased", Toast.LENGTH_SHORT).show();
            previousVolume = currentVolume;


            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
            String mac_list= sh.getString("mac_list","");



            String hu = sh.getString("url", "");
            String url = hu + "nearest_shop2";

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

                                    String res= jsonObj.getString("data");
                                    if (res.equalsIgnoreCase(""))
                                    {
                                        textToSpeech.speak("No information",TextToSpeech.QUEUE_FLUSH,null);
                                        Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        textToSpeech.speak(res,TextToSpeech.QUEUE_FLUSH,null);

                                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else {
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

                    String mac=sh.getString("mac_list","");
                    params.put("mac_list",sh.getString("mac_list",""));


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
        else if (delta < 0) {
            Toast.makeText(context, "Volume Increased", Toast.LENGTH_SHORT).show();
            previousVolume = currentVolume;
        }
    }

    private Context getApplicationContext() {
        return  context;
    }
}