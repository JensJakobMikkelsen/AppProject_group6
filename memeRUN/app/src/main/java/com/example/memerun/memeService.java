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
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.memerun.classes.achievement;
import com.example.memerun.classes.memeURL;
import com.example.memerun.customAdapter.SwipeAdapter;
import com.example.memerun.database.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class memeService extends Service {

    int timeCount = 0;
    int amountOfStepsRun = 0;
    int amountOfStepsWalked = 0;


    private WeakReference<Context> contextRef;
    DownLoadImageTask download;
    AppDatabase appDb;


    public memeService() {
    }


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public memeService getService() {
            // Return this instance of LocalService so clients can call public methods
            return memeService.this;
        }
    }

    Bitmap bmp;

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;

        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setImage(int position, ImageView img)
    {
        List<memeURL> urlList = appDb.daoAccess().getAllURLS();

        download = (DownLoadImageTask) new DownLoadImageTask(img)
                .execute(urlList.get(position).getURL());

        bmp = download.getBmImage_bm();

    }

    public Bitmap getBmp() {
        return bmp;
    }

    public List<achievement> getAchievements()
    {
        return appDb.daoAccess().getAll_Achievements();
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");

            if(message == "collection_init")
            {

                sendInitializationMessage();
            }
        }
    };






    @Override
    public void onCreate()
    {
        super.onCreate();

       LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));


        Intent notificationIntent = new Intent(this, MainActivity.class);

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

        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                timeCount++;
                handler.postDelayed(this, delay);
            }
        }, delay);


        //Create + init pedometer

        //amountOfStepsWalked = pedometer.getAmountOfStepsWalked();
        //amountOfStepsRun = pedometer.getAmountOfStepsRun();


        //Hvis volley?

      /*


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";



        // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Add the request to the RequestQueue.
                queue.add(stringRequest);





*/


    }

    public int getAmountOfStepsRun() {
        return amountOfStepsRun;
    }

    public int getAmountOfStepsWalked() {
        return amountOfStepsWalked;
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));


        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "Initialization_done");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
