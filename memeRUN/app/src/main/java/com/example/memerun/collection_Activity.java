package com.example.memerun;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.util.Collection;
import java.util.List;

public class collection_Activity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    Intent bound;
    boolean mBound = false;
    Bitmap bmp;
    public boolean execute = true;
    boolean stop = false;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

    boolean troll_pressed = false;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;
    boolean mShowVisible =false;


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

            case R.id.easy:

                mService.setMode(2);

                return true;

            case R.id.medium:

                mService.setMode(1);

                return true;

            case R.id.hard:

                mService.setMode(0.5);

                return true;

            case R.id.Set:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(collection_Activity.this);
                // set title
                alertDialogBuilder.setTitle("Set steps");

                final EditText input = new EditText (this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                final TextView input_ = new EditText (this);

                alertDialogBuilder.setView(input);


                // set dialog message
                alertDialogBuilder
                        .setMessage("NEJ")
                        .setCancelable(false)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mService.setSteps(Double.parseDouble(input.getText().toString()));
                                Toast.makeText(getApplicationContext(), "fatass",
                                        Toast.LENGTH_LONG).show();
                                mService.sendSensorUpdateMessage((int)mService.getSteps());
                                mService.checkAchievements(mService.getAchievements(), (int)mService.getSteps());

                                if(mService.getSteps() > 10000)
                                {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=d0aIqx1McVI"));
                                    startActivity(browserIntent);
                                }

                            }


                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

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

            else if (message.equals("Achievement unlocked!")) {

                if(execute) {

                    execute = false;

                    //https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android
                    //https://stackoverflow.com/questions/3263736/playing-a-video-in-videoview-in-android

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(collection_Activity.this);
                    LayoutInflater inflater = collection_Activity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.video_alert, null);

                    dialogBuilder.setView(dialogView);
                    final VideoView videoView = dialogView.findViewById(R.id.videoView_alert);
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.achievementunlocked);
                    videoView.setVideoURI(uri);

                    videoView.start();
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final Handler handler = new Handler();
                    final int delay = 250; //milliseconds

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                            if (!videoView.isPlaying()) {
                                alertDialog.dismiss();
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                                stop = true;
                                execute = true;
                            }
                            handler.postDelayed(this, delay);


                        }
                    }, delay);


                }

            }

            stop = false;
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

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }

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
