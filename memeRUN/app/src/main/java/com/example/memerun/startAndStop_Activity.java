package com.example.memerun;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import com.example.memerun.classes.achievement;

import java.util.ArrayList;
import java.util.List;

public class startAndStop_Activity extends AppCompatActivity {


    memeService mService;
    Intent bound;
    boolean mBound = false;
    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;
    boolean troll_pressed = false;

    final int textSize_big = 34;
    int textSize_count = 15;
    int textSize_stop = 0;
    final int textSize_small = 14;

    TextView TvSteps2;
    List<achievement> achievements = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.startandstop, menu);

        return true;
    }

    boolean mShowVisible =false;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.cheats).setVisible(mShowVisible);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        MenuItem cheat = findViewById(R.id.cheats);

        switch(item.getItemId())
        {
                case R.id.collection:

                    Intent collectionIntent = new Intent(startAndStop_Activity.this, collection_Activity.class);
                    startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
                    return true;

                case R.id.recent_activity:

                    Intent recentIntent = new Intent(startAndStop_Activity.this, recent_Activity.class);
                    startActivityForResult(recentIntent, STARTANDSTOPACTIVITY);
                    return true;

                case R.id.achievement:

                    Intent achievementIntent = new Intent(startAndStop_Activity.this, achievements_Activity.class);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(startAndStop_Activity.this);
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

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            memeService.LocalBinder binder = (memeService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.sendSensorUpdateMessage(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");

            int steps;
            String steps_s = "";
            if (message == "update") {
                steps = intent.getIntExtra("steps", 0);
                try {
                    steps_s = Integer.toString(steps);
                } catch (NumberFormatException nfe) {
                }

                TvSteps2 = findViewById(R.id.progress_text);
                TvSteps2.setText(steps_s);
                changeTextSize();

            }

            else if(message.equals("startAndStop___"))
            {
                achievements = mService.getAchievements();
                TextView next = findViewById(R.id.next_achievevement_txt);
                next.setText(achievements.get(0).getRequirement());
            }

            else if(message.equals("Achievement unlocked!"))
            {
                int number = intent.getIntExtra("number", 0);
                TextView next = findViewById(R.id.next_achievevement_txt);

                if(number < achievements.size()-1) {
                    next.setText(achievements.get(number + 1).getRequirement());
                }

                else
                {
                    next.setText("Everything is unlocked");
                }
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_and_stop_);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }
        /*
        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.walk);
        videoview.setVideoURI(uri);
        videoview.start();
*/

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        sendInitialization_recent();

        Button back = findViewById(R.id.start_and_stop_back_btn);
        Button start = findViewById(R.id.Start_and_stop_Start_btn);
        ImageView sad = findViewById(R.id.cancel_startAndStop_btn);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBound)
                {
                    mService.startStepSensor();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }

        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mService.stopStepSensor();
            }


        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }

    public void changeTextSize()
    {

        final Handler handler = new Handler();
        final long delay = 1; //milliseconds

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> waitTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (textSize_stop != 2) {
                            handler.postDelayed(this, delay);
                        }

                        if (textSize_stop == 0) {

                            textSize_count = textSize_big;
                            TvSteps2.setTextSize(textSize_count);
                            handler.postDelayed(this, 500);


                        } else if (textSize_stop == 1) {
                            TvSteps2.setTextSize(textSize_count);
                            textSize_count--;
                        }

                        if (textSize_count == textSize_big) {
                            textSize_stop = 1;
                        }

                        if (textSize_count == textSize_small) {
                            textSize_stop = 2;
                        }
                    }


                }, delay);

                return null;
            }

        }.execute();

        textSize_stop = 0;



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

    private void sendInitialization_recent()
    {
        Log.d("sender", "Initialization_done");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "init_startAndStop");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

