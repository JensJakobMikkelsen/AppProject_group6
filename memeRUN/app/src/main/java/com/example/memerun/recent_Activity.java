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
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.memerun.classes.recent;
import com.example.memerun.customAdapter.recentAdapter;

import java.util.List;

public class recent_Activity extends AppCompatActivity {

    recentAdapter adapter;
    ListView listView;

    Intent bound;
    boolean mBound = false;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;
    public boolean execute = true;
    boolean stop = false;

    boolean troll_pressed = false;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;
    boolean mShowVisible =false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recent, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.collection:

                Intent collectionIntent = new Intent(recent_Activity.this, collection_Activity.class);
                startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
                return true;

            case R.id.achievement:

                Intent achievementIntent = new Intent(recent_Activity.this, achievements_Activity.class);
                startActivityForResult(achievementIntent, RECENTACTIVITY);
                return true;

            case R.id.startAndStop:

                Intent startAndStopIntent = new Intent(recent_Activity.this, startAndStop_Activity.class);
                startActivityForResult(startAndStopIntent, STARTANDSTOPACTIVITY);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(recent_Activity.this);
                // set title
                String Setsteps = getString(R.string.Setsteps);
                String No = getString(R.string.No);
                String Set = getString(R.string.Set);
                String cancel = getString(R.string.cancel);
                alertDialogBuilder.setTitle(Setsteps);

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

    private memeService mService;
    private ServiceConnection mConnection;


    private void setupService() {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // http://developer.android.com/reference/android/app/Service.html
                mService = ((memeService.stockUpdateServiceBinder) service).getService();
                adapter.clear();
                List<recent> recentList = mService.getRecentActivity();

                if(recentList.size() != 0)
                {
                    for (int i = 0; i < recentList.size(); ++i) {
                        adapter.add(recentList.get(i));
                    }
                }

                prog = findViewById(R.id.progressbar_achievement);
                prog.setMax(mService.getAchievements().size());
                prog.setProgress(mService.getProgAmount());

                String progAmount_s = Integer.toString(mService.getProgAmount());

                progess_txt = findViewById(R.id.progress_bar_txt);
                String Achievements = getString(R.string.AchievementsUnlocked);
                progess_txt.setText(Achievements + progAmount_s + "/" + Integer.toString(prog.getMax()));

            }

            public void onServiceDisconnected(ComponentName className) {

                mService = null;
            }
        };
    }


    ProgressBar prog;
    TextView progess_txt;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message.equals("recent___"))
            {

            }

            else if (message.equals("progressbar_inc"))
            {
                prog.setProgress(mService.getProgAmount());
                String progAmount_s = Integer.toString(mService.getProgAmount());

                progess_txt = findViewById(R.id.progress_bar_txt);
                String Achievements = getString(R.string.AchievementsUnlocked);
                progess_txt.setText(Achievements + progAmount_s + "/" + Integer.toString(prog.getMax()));
            }

            else if (message.equals("Achievement unlocked!")) {

                if(execute) {

                    execute = false;

                    //https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android
                    //https://stackoverflow.com/questions/3263736/playing-a-video-in-videoview-in-android

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(recent_Activity.this);
                    LayoutInflater inflater = recent_Activity.this.getLayoutInflater();
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
                       //         alertDialog.dismiss();
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
        setContentView(R.layout.activity_recent_);

        setupService();

        Intent intent = new Intent(recent_Activity.this, memeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        listView = (ListView) findViewById(R.id.cardview_save);
        adapter = new recentAdapter(this);
        listView.setAdapter(adapter);

        sendInitMessage();

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }

        Button back = findViewById(R.id.back_recent_btn);

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

    private void sendInitMessage()
    {
        Log.d("sender", "Collection_started");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "recent_init");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }
}
