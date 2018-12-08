package com.example.memerun;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.Toolbar;
import android.widget.VideoView;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.classes.memesday;
import com.example.memerun.customAdapter.SwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    boolean stop = false;
    boolean mBound = false;

    boolean visibility1 = false;
    boolean visibility2;
    boolean troll_pressed = false;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;

    int min_ = 0;
    int max_ = 4;

    List<achievement> achievements = new ArrayList<>();
    public boolean execute = true;


    private memeService mService;
    private ServiceConnection mConnection;

    private void setupStockService() {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mService = ((memeService.stockUpdateServiceBinder) service).getService();

                achievements = mService.getAchievements();

                //Refreshes UI when connected to service
            }

            public void onServiceDisconnected(ComponentName className) {
                mService = null;
            }
        };

    }


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

                Intent collectionIntent = new Intent(MainActivity.this, collection_Activity.class);
                startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
                return true;

            case R.id.recent_activity:

                Intent recentIntent = new Intent(MainActivity.this, recent_Activity.class);
                startActivityForResult(recentIntent, STARTANDSTOPACTIVITY);
                return true;

            case R.id.achievement:

                Intent achievementIntent = new Intent(MainActivity.this, achievements_Activity.class);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
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


                // set dialog message
                alertDialogBuilder
                        .setMessage(No)
                        .setCancelable(false)
                        .setPositiveButton(Set, new DialogInterface.OnClickListener() {
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

            else if (message.equals("Achievement unlocked!")) {

                if(execute) {

                    execute = false;

                    //https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android
                    //https://stackoverflow.com/questions/3263736/playing-a-video-in-videoview-in-android

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
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
    MediaPlayer player;
    TextView memesday;

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
        memesday = findViewById(R.id.diary_txt);
        memesday.setText(timeStamp + ": " + quotes.get(i1));

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }


        Intent intent = new Intent(MainActivity.this, memeService.class);
        startService(intent);

        player = MediaPlayer.create(this, R.raw.fuckyou);

        Button start = findViewById(R.id.start_btn);
        Button recent = findViewById(R.id.Recent_activity_btn);
        Button achievements = findViewById(R.id.Achievements_btn);
        Button collection = findViewById(R.id.View_collection_btn);

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
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);
                            stop = true;
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

                        if(!stop) {
                            handler.postDelayed(this, delay);
                        }

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

    @Override
    protected void onStart() {

        super.onStart();

        setupStockService();

        Intent intent = new Intent(MainActivity.this, memeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;
    }

    public void setupConnection()
    {
        if (!mBound && mService == null) {

            setupStockService();

            Intent intent = new Intent(MainActivity.this, memeService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
        }

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
                setupConnection();
            }
        }

        if(requestCode == RECENTACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {
                setupConnection();
            }
        }

        if(requestCode == ACHIEVEMENTSACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {
                setupConnection();
            }
        }

        if(requestCode == COLLECTIONACTIVITY)
        {
            if(resultCode == RESULT_CANCELED)
            {
                setupConnection();
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.

        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("memesday", memesday.getText().toString());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        memesday.setText(savedInstanceState.getString("memesday"));
    }


}
