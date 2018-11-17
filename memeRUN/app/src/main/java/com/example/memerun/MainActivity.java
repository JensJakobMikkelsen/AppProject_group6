package com.example.memerun;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

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

    DownLoadImageTask download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        Button start = findViewById(R.id.start_btn);
        Button recent = findViewById(R.id.Recent_activity_btn);
        Button achievements = findViewById(R.id.Achievements_btn);
        Button collection = findViewById(R.id.View_collection_btn);

        final TextView retCount = findViewById(R.id.retCount);
        ImageView trollface = findViewById(R.id.trollface);

        //download = (DownLoadImageTask) new DownLoadImageTask((ImageView) findViewById(R.id.trollface))
        //        .execute("https://i.imgflip.com/10r5wh.jpg");


        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent collectionIntent = new Intent(getApplicationContext(), collection_Activity.class);
                startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
            }

        });

        trollface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                count = mService.retCount();
                retCount.setText(Integer.toString(count));
            }

        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startAndStopIntent = new Intent(getApplicationContext(), startAndStop_Activity.class);
                startActivityForResult(startAndStopIntent, STARTANDSTOPACTIVITY);
            }

        });


        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recent_activity = new Intent(getApplicationContext(), recent_Activity.class);
                startActivityForResult(recent_activity, RECENTACTIVITY);
            }

        });

        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent achievements = new Intent(getApplicationContext(), achievements_Activity.class);
                startActivityForResult(achievements, ACHIEVEMENTSACTIVITY);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == STARTANDSTOPACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {

            }
        }

        if(requestCode == RECENTACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {

            }
        }

        if(requestCode == ACHIEVEMENTSACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {

            }
        }

        if(requestCode == COLLECTIONACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {

            }
        }


    }
}
