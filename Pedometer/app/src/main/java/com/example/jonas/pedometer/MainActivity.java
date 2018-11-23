package com.example.jonas.pedometer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
// https://www.youtube.com/watch?v=CNGMWnmldaU
//public class MainActivity extends AppCompatActivity implements SensorEventListener {
public class MainActivity extends AppCompatActivity{

     SensorManager sensorManager;
    Sensor counterSensor;
    TextView TvSteps, TvSteps2;
    Button BtnStart2,BtnStop;
    private String TAG = MainActivity.class.getSimpleName();

    boolean running = false;
    int i;


    private Boolean serviceBound;
    private ServiceConnection BackserviceConnection;
    public Backgroundservice Backservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get an instance of the SensorManager

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
         counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvSteps2 = (TextView) findViewById(R.id.tv_steps2);
        BtnStart2 = (Button) findViewById(R.id.btn_start2);
        BtnStop = (Button) findViewById(R.id.btn_stop);

        BtnStart2.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View arg0) {
                bind();

                Log.d(TAG, "registering receivers");
                IntentFilter filter = new IntentFilter();
                //  filter.addAction(BackgroundService.BROADCAST_BACKGROUND_SERVICE_RESULT);
                LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onBackgroundServiceResult, filter);

            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(serviceBound) {
                    unbindService(BackserviceConnection);
                    serviceBound = false;
                    Log.d(TAG,"Service unbindService");
                }
            }
        });



        if(savedInstanceState != null)
        {
            i = savedInstanceState.getInt("time");
            running = savedInstanceState.getBoolean("running");
            TvSteps2.setText(String.valueOf(i));

        }

    }
void bind()
{
    //Setting up and binding to service
    Intent bindIntent = new Intent(this, Backgroundservice.class);
    startService(bindIntent);
    //This also sets up components
    setupConnectionBackservice();
    bindService(bindIntent, BackserviceConnection, Context.BIND_AUTO_CREATE);
}
    private void setupConnectionBackservice() {
        BackserviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                //Does this to get a concrete class and access it
                Backgroundservice.BackgroundServiceBinder binder = ( Backgroundservice.BackgroundServiceBinder) service;
                Backservice = binder.getService();
                serviceBound = true;
                Log.d(TAG,"Service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Backservice = null;
                serviceBound = false;
                Log.d(TAG,"Service disconnected");
            }
        };
    }
    public void onSaveInstanceState(Bundle outState) {


        outState.putInt("time",i);
        outState.putBoolean("running",running);
        super.onSaveInstanceState(outState);
    }

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String jsonStr = intent.getStringExtra(Backgroundservice.EXTRA_TASK_RESULT);
            Log.d(TAG, "Updating stocks..." +jsonStr );


            String results = intent.getStringExtra(Backgroundservice.EXTRA_TASK_RESULT);

            TvSteps2 = (TextView) findViewById(R.id.tv_steps2);
            TvSteps2.setText(results);

        }

    };

/*
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "registering receivers");
        IntentFilter filter = new IntentFilter();
        //  filter.addAction(BackgroundService.BROADCAST_BACKGROUND_SERVICE_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult, filter);
    }
/*

    @Override
    public void onSensorChanged(SensorEvent event) {

        TvSteps2.setText(String.valueOf(event.values[0]));
        i++;
        TvSteps2.setText(String.valueOf(i));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (running) {
            counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            //   if(counterSensor != null)
            sensorManager.registerListener(this, counterSensor, sensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    */
}