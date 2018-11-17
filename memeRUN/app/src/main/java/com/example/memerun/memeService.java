package com.example.memerun;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class memeService extends Service {

    int timeCount = 0;

    public memeService() {
    }


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public memeService getService() {
            // Return this instance of LocalService so clients can call public methods
            return memeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;

        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    //https://i.imgflip.com/10r5wh.jpg


    /*


     */

    @Override
    public void onCreate()
    {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sad_rage)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);

        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                timeCount++;
                handler.postDelayed(this, delay);
            }
        }, delay);



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
}
