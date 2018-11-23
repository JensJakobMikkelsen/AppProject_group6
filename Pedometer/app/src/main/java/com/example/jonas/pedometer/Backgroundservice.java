package com.example.jonas.pedometer;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Binder;

import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import java.net.URL;


public class Backgroundservice extends Service  implements SensorEventListener {


    public static final String EXTRA_TASK_RESULT = "result";
    private static final String LOG = "BG_SERVICE";
    private boolean started = false;
    private IBinder binder = new BackgroundServiceBinder();
    private String TAG = Backgroundservice.class.getSimpleName();
    int i;

    SensorManager sensorManager;
    public Sensor counterSensor;

    public Backgroundservice() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Log.d(TAG, "QuoteService Started");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
   //     new updatestocks().execute();

        counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        //   if(counterSensor != null)
        sensorManager.registerListener((SensorEventListener) this, counterSensor, sensorManager.SENSOR_DELAY_FASTEST);
        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class updatestocks extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... arg0) {

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            //   if(counterSensor != null)
            sensorManager.registerListener((SensorEventListener) this, counterSensor, sensorManager.SENSOR_DELAY_FASTEST);
            return null;
        }

    }
    public void onSensorChanged(SensorEvent event) {


        i++;
        broadcastTaskResult(String.valueOf(i));

    }
    private void broadcastTaskResult(String result){
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(EXTRA_TASK_RESULT, result);
        Log.d(LOG, "Broadcasting:" + result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    // Method taken directly from lecture example code "servicesDemo"
    @Override
    public void onDestroy() {
        started = false;
        Log.d(LOG,"Background service destroyed");
        super.onDestroy();
    }

    public class BackgroundServiceBinder extends Binder {
        public   Backgroundservice getService() {
            // Return this instance of LocalService so clients can call public methods
            return Backgroundservice.this;
        }
    }


}

