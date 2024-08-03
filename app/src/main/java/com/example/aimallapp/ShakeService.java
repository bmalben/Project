package com.example.aimallapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ShakeService extends Service {
    TextToSpeech t1;

    private boolean mIsServiceStarted = false;
    private Context mContext = null;
    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    private File mLogFile = null;
    private FileOutputStream mFileStream = null;
    private Float[] mValues = null;
    private long mTimeStamp = 0;
    private ExecutorService mExecutor = null;


    private static final int RUN_THRESHOLD = 4000;
    private static final int WAL_THRESHOLD = 2800;

    /**
     * Default empty constructor needed by Android OS
     */
    public ShakeService() {
        super();
    }

    /**
     * Constructor which takes context as argument
     *
     * @param context
     */
    public ShakeService(Context context) {
        super();

        if (context != null)
            mContext = context;
        else
            mContext = getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        Toast.makeText(getBaseContext(), "Service onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isServiceStarted() == false) {

            mContext = getBaseContext();
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mValues = new Float[]{0f, 0f, 0f};
            mTimeStamp = 0;
            mExecutor = Executors.newSingleThreadExecutor();

//            setupFolderAndFile();
            startLogging();
        }

        //set started to true
        mIsServiceStarted = true;


        Toast.makeText(mContext, "Service onStartCommand", Toast.LENGTH_SHORT).show();
        return Service.START_STICKY;
    }

    private void setupFolderAndFile() {
        mLogFile = new File(Environment.getExternalStorageDirectory().toString()
                + "/test.txt");

        try {
            mFileStream = new FileOutputStream(mLogFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 2500


            ;

    private void startLogging() {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mSensorManager.registerListener(
                        new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent sensorEvent) {
//                                mTimeStamp = System.currentTimeMillis();
                                long curTime = System.currentTimeMillis();
                                // only allow one update every 100ms.
                                if ((curTime - lastUpdate) > 100) {
                                    long diffTime = (curTime - lastUpdate);
                                    lastUpdate = curTime;
                                    String formatted = String.valueOf(mTimeStamp)
                                            + "\t" + String.valueOf(mValues[0])
                                            + "\t" + String.valueOf(mValues[1])
                                            + "\t" + String.valueOf(mValues[2])
                                            + "\r\n";

//                                mValues[0] = sensorEvent.values[0];
//                                mValues[1] = sensorEvent.values[1];
//                                mValues[2] = sensorEvent.values[2];
                                    x= mValues[0] = sensorEvent.values[0];
                                    y= mValues[1] = sensorEvent.values[1];
                                    z= mValues[2] = sensorEvent.values[2];

                                    float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                                    String road_cond="";

//                                    if (speed >2000 && speed<4000)
//                                    {
//                                        road_cond="normal speed";
////                                        road_condition(road_cond);
//                                    }
                                     if(speed > 4000){
                                        road_cond="Accident";
                                        road_condition(road_cond);

                                    }

                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int i) {

                            }
                        }, mSensor, SensorManager.SENSOR_DELAY_FASTEST
                );
            }
        });
    }

    public void road_condition(String road_cond){
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress", "");
        String  apiURL = sh.getString("url", "") + "emergency";


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//                                String msg = jsonObj.getString("message");
//                                Toast.makeText(MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                try {
//                                    Toast.makeText(mContext, "hiiiiiiii", Toast.LENGTH_SHORT).show();
//                                                                    Speakerbox sp = new Speakerbox(getApplication());

                                    t1.speak( "Emergency message send to Guard.....", TextToSpeech.QUEUE_FLUSH, null);
                                }
                                catch (Exception e){
                                    Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(getApplicationContext(), "invalid.....", Toast.LENGTH_SHORT).show();


                            // }
                            ///
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
                String voice = sh.getString("voice", "");
                params.put("road_cond", road_cond);
                params.put("lid", sh.getString("lid", ""));
                params.put("lati", gpstracker.lati);
                params.put("longi", gpstracker.longi);
                params.put("place", gpstracker.place);
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 10000000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Flush and close file stream
        if (mFileStream != null) {
            try {
                mFileStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mFileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(mContext, "Service onDestroy", Toast.LENGTH_LONG).show();
        mIsServiceStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Indicates if service is already started or not
     *
     * @return
     */
    public boolean isServiceStarted() {
        return mIsServiceStarted;
    }

    public void saveActivity(String act) {
//        SQLiteDatabase obj=openOrCreateDatabase("friendbook", SQLiteDatabase.CREATE_IF_NECESSARY,null);
//
//        String qry="create table if not exists activities(id integer PRIMARY KEY AUTOINCREMENT,act text,acount integer,adate text,atm text)";
//        obj.execSQL(qry);
//
//        SimpleDateFormat sm=new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat st=new SimpleDateFormat("hh:mm:ss a");
//        String dt=sm.format(new Date());
//        String tm=sm.format(new Date());
//
//        String query="select * from activities where act='"+act+"'";
//        Cursor cr=obj.rawQuery(query, null);
//        Log.d("",cr.getCount()+"");
//
//        if(cr!=null&&cr.getCount()>0)
//        {
//            while(cr.moveToNext()){
//                Log.d("",cr.getString(2)+"");
//                ContentValues cv=new ContentValues();
//                cv.put("act", act);
//                cv.put("acount", cr.getInt(2)+1);
//                cv.put("adate", dt);
//                cv.put("atm", tm);
//                obj.update("activities",cv, "act=?", new String[]{act});
//            }
//        }
//        else{
//            ContentValues cv=new ContentValues();
//            cv.put("act", act);
//            cv.put("acount", 1);
//            cv.put("adate", dt);
//            cv.put("atm", tm);
//            obj.insert("activities", null, cv);
//        }
//        cr.close();
//        obj.close();
    }

}