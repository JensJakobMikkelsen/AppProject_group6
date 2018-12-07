package com.example.memerun;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.util.List;

public class collection_Activity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    Intent bound;
    boolean mBound = false;
    Bitmap bmp;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.collection, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.startAndStop:

                Intent startAndStopIntent = new Intent(collection_Activity.this, startAndStop_Activity.class);
                startActivityForResult(startAndStopIntent, STARTANDSTOPACTIVITY);
                return true;

            case R.id.recent_activity:

                Intent recentIntent = new Intent(collection_Activity.this, recent_Activity.class);
                startActivityForResult(recentIntent, RECENTACTIVITY);
                return true;

            case R.id.achievement:

                Intent achievementIntent = new Intent(collection_Activity.this, achievements_Activity.class);
                startActivityForResult(achievementIntent, ACHIEVEMENTSACTIVITY);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private memeService mService;
    private ServiceConnection mConnection;

    private void setupService() {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // http://developer.android.com/reference/android/app/Service.html
                mService = ((memeService.stockUpdateServiceBinder) service).getService();
                //Refreshes UI when connected to service
            }

            public void onServiceDisconnected(ComponentName className) {

                mService = null;
            }
        };
    }

    SwipeAdapter swipeAdapter;
    ViewPager viewPager;


    int swipePosition;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message == "Initialization_done") {

                List<bitmapCounter> tempBitmapList = mService.getBmList();

                viewPager = (ViewPager)findViewById(R.id.viewPager);
                viewPager.setOffscreenPageLimit(1);

                Bitmap not_unlocked = BitmapFactory.decodeResource(getResources(), R.mipmap.not_unlocked);
                swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), tempBitmapList, mService.getAchievements(), not_unlocked);

                viewPager.setAdapter(swipeAdapter);
                viewPager.setCurrentItem(0);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }
                    @Override
                    public void onPageSelected(int position) {

                        swipePosition = position;
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

            }
        }
    };

    private void sendInitMessage()
    {
        Log.d("sender", "Collection_started");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "collection_init");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_);

        setupService();

        Intent intent = new Intent(collection_Activity.this, memeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        Button back = findViewById(R.id.back_btn_collection);
  //      Button save = findViewById(R.id.btn_save_collection);

        sendInitMessage();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STARTANDSTOPACTIVITY) {
            if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == RECENTACTIVITY) {
            if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == ACHIEVEMENTSACTIVITY) {
            if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == COLLECTIONACTIVITY) {
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }

    //Skalerer et bitmap
    /*

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    */


}
