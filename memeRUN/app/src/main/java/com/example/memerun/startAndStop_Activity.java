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
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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

import com.example.memerun.classes.achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class startAndStop_Activity extends AppCompatActivity {


    private memeService mService;
    private ServiceConnection mConnection;
    boolean mBound = false;
    int only_once = 0;



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

    public boolean execute = true;
    public boolean stop = false;

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
                String Setsteps = getString(R.string.Setsteps);
                String No = getString(R.string.No);
                String Set = getString(R.string.Set);
                String cancel = getString(R.string.cancel);
                alertDialogBuilder.setTitle(Setsteps);

                final EditText input = new EditText (this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                final TextView input_ = new EditText (this);

                alertDialogBuilder.setView(input);


                // https://stackoverflow.com/questions/12993992/issue-with-alert-dialog-with-two-buttons-cancel-and-open-link-activity

                // set dialog message
                alertDialogBuilder
                        .setMessage(No)
                        .setCancelable(false)
                        .setPositiveButton(Set, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!(input.getText().toString().isEmpty())) {

                                    mService.setSteps(Double.parseDouble(input.getText().toString()));
                                    Toast.makeText(getApplicationContext(), "fatass",
                                            Toast.LENGTH_LONG).show();
                                    mService.sendSensorUpdateMessage((int) mService.getSteps());
                                    mService.checkAchievements(mService.getAchievements(), (int) mService.getSteps());

                                    if (mService.getSteps() > 10000) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=d0aIqx1McVI"));
                                        startActivity(browserIntent);
                                    }
                                }

                            }


                        })
                        .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
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

    private void setupService() {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // http://developer.android.com/reference/android/app/Service.html
                mService = ((memeService.stockUpdateServiceBinder) service).getService();

                TextView txt = findViewById(R.id.progress_text);
                txt.setText(Double.toString(mService.getSteps()));

                achievements = mService.getAchievements();

                int number = mService.getUnlockedPosition();
                TextView next = findViewById(R.id.next_achievevement_txt);

                if(number == 0)
                {
                    String mess = getResources().getString(R.string.Steps);
                    int steps = achievements.get(0).getSteps();
                    String steps_s = Integer.toString(steps);
                    next.setText(steps_s + " " + mess);
                }

                else if (number < achievements.size() - 1) {

                    String mess = getResources().getString(R.string.Steps);
                    int steps = achievements.get(number + 1).getSteps();
                    String steps_s = Integer.toString(steps);
                    next.setText(steps_s + " " + mess);
                }

                else {
                    next.setText("Everything is unlocked");
                }


            }

            public void onServiceDisconnected(ComponentName className) {

                mService = null;
            }
        };
    }

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

            } else if (message.equals("startAndStop___")) {

            } else if (message.equals("Achievement unlocked!")) {
                int number = intent.getIntExtra("number", 0);
                TextView next = findViewById(R.id.next_achievevement_txt);

                if (number < achievements.size() - 1) {
                    next.setText(achievements.get(number + 1).getRequirement());
                } else {
                    next.setText("Everything is unlocked");
                }

                if(execute) {

                    execute = false;

                    //https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android
                    //https://stackoverflow.com/questions/3263736/playing-a-video-in-videoview-in-android

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(startAndStop_Activity.this);
                    LayoutInflater inflater = startAndStop_Activity.this.getLayoutInflater();
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

                            if (!videoView.isPlaying()) {
                 //               alertDialog.dismiss();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_and_stop_);

        setupService();

        Intent intent = new Intent(startAndStop_Activity.this, memeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }

        sendInitialization_recent();

        Button back = findViewById(R.id.startandstop_back);
        Button start = findViewById(R.id.startandstop_start);
        Button stop = findViewById(R.id.startandstop_cancel);


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

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String  stop =  findViewById(R.string.stop);
                String stop = getResources().getString(R.string.stop);
                mService.stopStepSensor();
                Toast.makeText(getApplicationContext(),stop ,
                        Toast.LENGTH_LONG).show();


            }


        });

    }
    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));

        if(mBound && mService != null)
        {
            TextView txt = findViewById(R.id.progress_text);
            txt.setText(Double.toString(mService.getSteps()));
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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

