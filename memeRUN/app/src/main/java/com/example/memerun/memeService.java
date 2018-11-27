package com.example.memerun;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.memerun.classes.achievement;
import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.classes.memeURL;
import com.example.memerun.classes.recent;
import com.example.memerun.customAdapter.SwipeAdapter;
import com.example.memerun.database.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class memeService extends Service implements SensorEventListener {

    Context context_;

    int timeCount = 0;
    int amountOfStepsRun = 0;
    int amountOfStepsWalked = 0;

    SensorManager sensorManager;
    public Sensor counterSensor;

    private WeakReference<Context> contextRef;
    AppDatabase appDb;
    List<achievement> achievementList;
    List<recent> recentList = new ArrayList<>();
    public static final String myPreferences = "MyPrefs";

    public memeService() {
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public memeService getService() {
            // Return this instance of LocalService so clients can call public methods
            return memeService.this;
        }
    }

    public List<recent> getRecentActivity() {
        return recentList;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void checkAchievements(List<achievement> achievementList, int steps)
    {
        for(int i = 0; i < achievementList.size(); ++i)
        {
            if(achievementList.get(i).getSteps() == steps)
            {
                if(!achievementList.get(i).isUnlocked()) {

                    achievementList.get(i).setUnlocked(true);

                    Toast.makeText(getApplicationContext(), "Achievement unlocked!",
                            Toast.LENGTH_LONG).show();
                    appDb.daoAccess().update(achievementList.get(i));
                }

            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    int steps;
    private void sendSensorUpdateMessage(int steps)
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "update");
        intent.putExtra("steps", steps);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    List<bitmapCounter> bmList = new ArrayList<>();

    public List<bitmapCounter> getBmList() {
        return bmList;
    }


    public void initImageList() {

        List<memeURL> memeList = appDb.daoAccess().getAllURLSbyID();

        List<memeURL> tempMemeList = new ArrayList<>();

        for(int i = 0; i < 10; ++i)
        {
            tempMemeList.add(null);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        for(int i = 0; i < memeList.size(); i++) {

            final int localI = i;

            ImageRequest ir = new ImageRequest(memeList.get(i).getURL(),
                    new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(Bitmap response) {

                            bmList.add(new bitmapCounter(response, localI));

                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                }
            });

            requestQueue.add(ir);
        }
};


    public List<achievement> getAchievements()
    {
        return achievementList;
    }

    int onlyOnce = 0;
    SharedPreferences sharedPreferences;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");

            if(message == "databasePopulated")
            {
                if(onlyOnce == 0)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            AppDatabase VolleappDb = AppDatabase.getDatabase(getApplicationContext());
                            List<memeURL> memeList = appDb.daoAccess().getAllURLS();
                            achievementList = appDb.daoAccess().getAll_AchievementsByID();
                            recentList = appDb.daoAccess().getAll_recentByID();

                            initImageList();

                            onlyOnce = 1;

                            sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("onlyOnce", onlyOnce);
                            editor.apply();

                        }
                    }).start();
                }
            }

            else if(message == "collection_init")
            {
                sendInitializationMessage();
            }

            else if(message == "achievement_init")
            {
                sendInitialization_achievements();
            }

            else if(message == "recent_init")
            {
                sendInitialization_recent();
            }


        }
    };

    public void retrieve()
    {
        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        onlyOnce = sharedPreferences.getInt("onlyOnce", 0);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        context_ = getApplicationContext();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));
        Intent notificationIntent = new Intent(this, startAndStop_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sad_rage)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();
        startForeground(1337, notification);


        contextRef = new WeakReference<>(getApplicationContext());

        //Database callback happens only if getDatabase is called
        appDb = AppDatabase.getDatabase(contextRef.get());
        appDb.daoAccess().getAllURLS();

        retrieve();

        if(onlyOnce == 1)
        {
            AppDatabase VolleappDb = AppDatabase.getDatabase(getApplicationContext());
            List<memeURL> memeList = appDb.daoAccess().getAllURLS();
            achievementList = appDb.daoAccess().getAll_AchievementsByID();
            recentList = appDb.daoAccess().getAll_recentByID();
            initImageList();
        }



        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                timeCount++;
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    public int retCount()
    {
        return timeCount;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_LONG).show();

    }

    private void sendInitializationMessage()
    {

        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "Initialization_done");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendInitialization_achievements()
    {

        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "achievements___");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendInitialization_recent()
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "recent___");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void startStepSensor()
    {
        sensorManager.registerListener(this, counterSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopStepSensor()
    {
        if(steps != 0) {
            recent recent_ = new recent(steps);
            recentList.add(recent_);
            appDb.daoAccess().insert(recent_);

            steps = 0;
        }
        sensorManager.unregisterListener(this);

    }

    public void onSensorChanged(SensorEvent event) {
        steps++;
        sendSensorUpdateMessage(steps);
        checkAchievements(achievementList, steps);

    }

}
