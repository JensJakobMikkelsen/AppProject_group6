package com.example.memerun;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class startAndStop_Activity extends AppCompatActivity {

    int STARTANDSTOPACTIVITY = 111;

    memeService mService;
    Intent bound;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            memeService.LocalBinder binder = (memeService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    final int textSize_big = 34;
    int textSize_count = 15;
    int textSize_stop = 0;
    final int textSize_small = 14;

    TextView TvSteps2;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");

            int steps;
            String steps_s = "";
            if (message == "update") {
                steps = intent.getIntExtra("steps", 0);
                try {
                    steps_s = Integer.toString(steps);
                } catch (NumberFormatException nfe) {
                }

                TvSteps2 = findViewById(R.id.progress_text);
                TvSteps2.setText(steps_s);

                changeTextSize();

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_and_stop_);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);


        Button back = findViewById(R.id.start_and_stop_back_btn);
        Button start = findViewById(R.id.Start_and_stop_Start_btn);

        ImageView sad = findViewById(R.id.cancel_startAndStop_btn);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBound)
                {
                    mService.startStepSensor();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }

        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mService.stopStepSensor();
            }


        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }

    public void changeTextSize()
    {

        final Handler handler = new Handler();
        final long delay = 1; //milliseconds

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> waitTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (textSize_stop != 2) {
                            handler.postDelayed(this, delay);
                        }

                        if (textSize_stop == 0) {

                            textSize_count = textSize_big;
                            TvSteps2.setTextSize(textSize_count);
                            handler.postDelayed(this, 500);


                        } else if (textSize_stop == 1) {
                            TvSteps2.setTextSize(textSize_count);
                            textSize_count--;
                        }

                        if (textSize_count == textSize_big) {
                            textSize_stop = 1;
                        }

                        if (textSize_count == textSize_small) {
                            textSize_stop = 2;
                        }
                    }


                }, delay);

                return null;
            }

        }.execute();

        textSize_stop = 0;



    }

}

