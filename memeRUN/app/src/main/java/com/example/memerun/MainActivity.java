package com.example.memerun;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import com.example.memerun.classes.memesday;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;


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
    boolean troll_pressed = false;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;

    int min_ = 0;
    int max_ = 4;




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

                if(player.isPlaying()) {
                    if (placement.equals("left")) {
                        ImageView troll = findViewById(R.id.troll_left);
                        troll.setVisibility(View.INVISIBLE);
                    } else if (placement.equals("top")) {
                        ImageView troll = findViewById(R.id.troll_top);
                        troll.setVisibility(View.INVISIBLE);
                    } else if (placement.equals("top_right")) {
                        ImageView troll = findViewById(R.id.troll_top_right);
                        troll.setVisibility(View.INVISIBLE);
                    } else if (placement.equals("right")) {
                        ImageView troll = findViewById(R.id.troll_right);
                        troll.setVisibility(View.INVISIBLE);

                    } else if (placement.equals("bottom")) {
                        ImageView troll = findViewById(R.id.troll_bottom);
                        troll.setVisibility(View.INVISIBLE);

                    }
                }

            }

            else if(message.equals("show"))
            {
                Bitmap bmap = intent.getParcelableExtra("bmap");

                if(player.isPlaying()) {

                    if (placement.equals("left")) {
                        ImageView troll = findViewById(R.id.troll_left);
                        troll.setVisibility(View.VISIBLE);
                    } else if (placement.equals("top")) {
                        ImageView troll = findViewById(R.id.troll_top);
                        troll.setVisibility(View.VISIBLE);
                    } else if (placement.equals("top_right")) {
                        ImageView troll = findViewById(R.id.troll_top_right);
                        troll.setVisibility(View.VISIBLE);
                    } else if (placement.equals("right")) {
                        ImageView troll = findViewById(R.id.troll_right);
                        troll.setVisibility(View.VISIBLE);
                    } else if (placement.equals("bottom")) {
                        ImageView troll = findViewById(R.id.troll_bottom);
                        troll.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memesday memesday_ = new memesday();
        List<String> quotes = memesday_.getMemesday();

        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = getDateCurrentTimeZone(tsLong);

        int min = 0;
        int max = quotes.size()-1;
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        TextView memesday = findViewById(R.id.diary_txt);
        memesday.setText(timeStamp + ": " + quotes.get(i1));



        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        player = MediaPlayer.create(this, R.raw.fuckyou);

        Button start = findViewById(R.id.start_btn);
        Button recent = findViewById(R.id.Recent_activity_btn);
        Button achievements = findViewById(R.id.Achievements_btn);
        Button collection = findViewById(R.id.View_collection_btn);


        //final TextView retCount = findViewById(R.id.retCount);
        ImageView trollface = findViewById(R.id.trollface);

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

                setContentView(R.layout.troll_layout);

                troll_pressed = true;

                sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("troll_pressed", troll_pressed);
                editor.apply();

                player.start();

                final Handler handler = new Handler();
                final int delay = 250; //milliseconds

                handler.postDelayed(new Runnable(){
                    public void run(){
                        //do something

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                        if(!player.isPlaying())
                        {
                            System.exit(0);
                        }

                        if(visibility1) {

                            for(int i = 0; i < 5; ++i)
                            {
                                Random r = new Random();
                                int i1 = r.nextInt(max_ - min_ + 1) + min_;

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
                                int i1 = r.nextInt(max_ - min_ + 1) + min_;

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

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

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


    //Fra stackOverflow

    public String getDateCurrentTimeZone(long timestamp) {
        try{

            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("Denmark");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        }catch (Exception e) {
        }
        return "";
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
