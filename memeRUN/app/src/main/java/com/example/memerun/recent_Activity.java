package com.example.memerun;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.memerun.classes.recent;
import com.example.memerun.customAdapter.recentAdapter;

import java.util.List;

public class recent_Activity extends AppCompatActivity {

    recentAdapter adapter;
    ListView listView;

    memeService mService;
    Intent bound;
    boolean mBound = false;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

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

            case R.id.startandstop:

                Intent startAndStopIntent = new Intent(recent_Activity.this, startAndStop_Activity.class);
                startActivityForResult(startAndStopIntent, STARTANDSTOPACTIVITY);
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    ProgressBar prog;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message.equals("recent___"))
            {
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


            }

            else if (message.equals("progressbar_inc"))
            {
                prog.setProgress(mService.getProgAmount());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_);

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        listView = (ListView) findViewById(R.id.cardview_save);
        adapter = new recentAdapter(this);
        listView.setAdapter(adapter);


        sendInitMessage();





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

    protected void onStop()
    {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(mConnection);

    }
}
