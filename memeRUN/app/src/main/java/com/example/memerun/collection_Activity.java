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

import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.util.List;

public class collection_Activity extends AppCompatActivity {


    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;


    memeService mService;
    Intent bound;
    boolean mBound = false;
    Bitmap bmp;

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
            if (message == "Initialization_done") {

                //Sker en enkelt gang efter init

                ImageView volley = findViewById(R.id.volleyUrl);
                setBitmapByNumber(3, volley);

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
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_);


        //Adnaan kode start

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(0);

        //Adnaan kode slut


        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        Button back = findViewById(R.id.Back_collection_btn);
        final Button saveToPhone = findViewById(R.id.save_image);

        sendInitMessage();

        saveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saveToExternalStorage(bmap);



                //Test af setBitmapByNumber, kan fjernes
                /*

                ImageView volley = findViewById(R.id.volleyUrl);
                setBitmapByNumber(4, volley);

                */

            }

        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }

        });

    }

    void setBitmapByNumber(int position, ImageView img)
    {
        List<bitmapCounter> tempBitmapList = mService.getBmList();
        for(int i = 0; i < tempBitmapList.size(); ++i)
        {
            if(tempBitmapList.get(i).getNumberOfBitmap() == position)
            {
                img.setImageBitmap(tempBitmapList.get(i).getBm());
            }
        }
    }

    // https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android

    private void saveToExternalStorage(Bitmap bmp) {


        // https://developer.android.com/training/permissions/requesting


        /*
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        */

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(collection_Activity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(collection_Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "test" , "test");
        }

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
