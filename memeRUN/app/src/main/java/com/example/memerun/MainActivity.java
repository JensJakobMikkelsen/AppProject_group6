package com.example.memerun;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

    memeService mService;
    Intent bound;
    boolean mBound = false;

    boolean visibility1 = false;
    boolean visibility2;

    int min = 0;
    int max = 4;


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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            String placement = intent.getStringExtra("placement");

            if (message.equals("hide")) {

                if(placement.equals("left"))
                {
                    ImageView troll = findViewById(R.id.troll_left);
                    troll.setVisibility(View.INVISIBLE);
                }

                else if (placement.equals("top"))
                {
                    ImageView troll = findViewById(R.id.troll_top);
                    troll.setVisibility(View.INVISIBLE);
                }

                else if (placement.equals("top_right"))
                {
                    ImageView troll = findViewById(R.id.troll_top_right);
                    troll.setVisibility(View.INVISIBLE);
                }

                else if (placement.equals("right"))
                {
                    ImageView troll = findViewById(R.id.troll_right);
                    troll.setVisibility(View.INVISIBLE);

                }

                else if (placement.equals("bottom"))
                {
                    ImageView troll = findViewById(R.id.troll_bottom);
                    troll.setVisibility(View.INVISIBLE);

                }

            }

            else if(message.equals("show"))
            {
                Bitmap bmap = intent.getParcelableExtra("bmap");


                if(placement.equals("left"))
                {
                    ImageView troll = findViewById(R.id.troll_left);
                    troll.setVisibility(View.VISIBLE);
                }

                else if (placement.equals("top"))
                {
                    ImageView troll = findViewById(R.id.troll_top);
                    troll.setVisibility(View.VISIBLE);
                }

                else if (placement.equals("top_right"))
                {
                    ImageView troll = findViewById(R.id.troll_top_right);
                    troll.setVisibility(View.VISIBLE);
                }

                else if (placement.equals("right"))
                {
                    ImageView troll = findViewById(R.id.troll_right);
                    troll.setVisibility(View.VISIBLE);
                }

                else if (placement.equals("bottom"))
                {
                    ImageView troll = findViewById(R.id.troll_bottom);
                    troll.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        final MediaPlayer player = MediaPlayer.create(this, R.raw.fuckyou);

        Button start = findViewById(R.id.start_btn);
        Button recent = findViewById(R.id.Recent_activity_btn);
        Button achievements = findViewById(R.id.Achievements_btn);
        Button collection = findViewById(R.id.View_collection_btn);
        Button ret = findViewById(R.id.ret_btn);

        final TextView retCount = findViewById(R.id.retCount);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBound)
                {
                    retCount.setText(Integer.toString(mService.retCount()));
                }

            }

        });

        //final TextView retCount = findViewById(R.id.retCount);
        ImageView trollface = findViewById(R.id.trollface);

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent collectionIntent = new Intent(getApplicationContext(), collection_Activity.class);
                startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
            }

        });

        final Handler handler = new Handler();
        trollface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean visibility2;

                int count = 0;
                count = mService.retCount();
                //retCount.setText(Integer.toString(count));
                setContentView(R.layout.troll_layout);
                player.start();

                final Handler handler = new Handler();
                final int delay = 250; //milliseconds

                handler.postDelayed(new Runnable(){
                    public void run(){
                        //do something

                        if(!player.isPlaying())
                        {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }

                        if(visibility1) {

                            for(int i = 0; i < 5; ++i)
                            {
                                Random r = new Random();
                                int i1 = r.nextInt(max - min + 1) + min;

                                switch(i1) {
                                    case 0:
                                        sendHideMessage("left");
                                        break;
                                    case 1:
                                        sendHideMessage("top");
                                        break;
                                    case 2:
                                        sendHideMessage("bottom");
                                        break;
                                    case 3:
                                        sendHideMessage("top_right");
                                        break;
                                    case 4:
                                        sendHideMessage("right");
                                        break;

                                    default:
                                        break;
                                }

                            }
                        }


                        else
                        {

                            for(int i = 0; i < 5; ++i)
                            {
                                Random r = new Random();
                                int i1 = r.nextInt(max - min + 1) + min;

                                switch(i1) {
                                    case 0:
                                        sendShowMessage("left");
                                        break;
                                    case 1:
                                        sendShowMessage("top");
                                        break;
                                    case 2:
                                        sendShowMessage("bottom");
                                        break;
                                    case 3:
                                        sendShowMessage("top_right");
                                        break;
                                    case 4:
                                        sendShowMessage("right");
                                        break;

                                    default:
                                        break;
                                }

                            }


                        }


                        visibility1 = !visibility1;

                        handler.postDelayed(this, delay);

                    }
                }, delay);


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
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));
    }

    private void sendHideMessage(String placement)
    {

        Log.d("sender", "Collection_started");
        Intent intent = new Intent("memeService");
        intent.putExtra("placement", placement);
        intent.putExtra("message", "hide");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendShowMessage(String placement)
    {
        Log.d("sender", "Collection_started");
        Intent intent = new Intent("memeService");
        intent.putExtra("placement", placement);
        intent.putExtra("message", "show");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }
}
