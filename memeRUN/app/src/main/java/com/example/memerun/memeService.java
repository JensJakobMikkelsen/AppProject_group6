package com.example.memerun;

import android.app.Notification;
import android.app.NotificationManager;
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
import android.media.MediaPlayer;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class memeService extends Service implements SensorEventListener {

    Context context_;

    int timeCount = 0;
    SensorManager sensorManager;
    public Sensor counterSensor;

    private WeakReference<Context> contextRef;
    AppDatabase appDb;
    List<achievement> achievementList;
    List<recent> recentList = new ArrayList<>();
    public static final String myPreferences = "MyPrefs";
    double mode = 1;
    int progAmount = 0;

    public int getProgAmount() {
        return progAmount;
    }

    public void setProgAmount(int progAmount) {
        this.progAmount = progAmount;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }

    public double getMode() {
        return mode;
    }

    public void setMode(double mode) {
        this.mode = mode;
    }

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

        boolean unlocked = false;

        for(int i = 0; i < achievementList.size(); ++i)
        {
            if(achievementList.get(i).getSteps() <= steps)
            {
                if(!achievementList.get(i).isUnlocked()) {

                    achievementList.get(i).setUnlocked(true);
                    send_achievement_unlocked(i);
                    appDb.daoAccess().update(achievementList.get(i));
                    unlocked = true;

                    progAmount++;
                    send_progessbar_inc();
                }

            }
        }

        if(unlocked) {

            Intent notificationIntent = new Intent(this, achievements_Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("memeRUN")
                    .setContentText("Achievement unLOCKED!!!!")
                    .setContentIntent(pendingIntent).build();

            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, b.build());
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    double steps;
    public void sendSensorUpdateMessage(int steps)
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "update");
        intent.putExtra("steps", steps);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    List<bitmapCounter> bmList = new ArrayList<>();

    public List<bitmapCounter> getBmList() {

        List<bitmapCounter> tempBmList = new ArrayList<>();
        List<achievement> tempAchievementList = new ArrayList<>();

        for(int i = 0; i < bmList.size(); ++i)
        {

        }

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

            else if(message.equals("collection_init"))
            {
                sendInitializationMessage();
            }

            else if(message.equals("achievement_init"))
            {
                sendInitialization_achievements();
            }

            else if(message.equals("recent_init"))
            {
                sendInitialization_recent();
            }

            else if(message.equals("init_startAndStop"))
            {
                sendInitialization_startAndStop();
            }


        }
    };

    double firstTime;

    public void retrieve()
    {
        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        onlyOnce = sharedPreferences.getInt("onlyOnce", 0);
    }

    Notification notification;

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

        notification = new NotificationCompat.Builder(this)
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

    private void sendInitialization_startAndStop()
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "startAndStop___");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void send_achievement_unlocked(int number)
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "Achievement unlocked!");
        intent.putExtra("number", number);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void send_progessbar_inc()
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "progressbar_inc");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void startStepSensor()
    {
        sensorManager.registerListener(this, counterSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }



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

        steps += 1 * mode;

        sendSensorUpdateMessage((int) steps);
        checkAchievements(achievementList, (int)steps);

    }

}
