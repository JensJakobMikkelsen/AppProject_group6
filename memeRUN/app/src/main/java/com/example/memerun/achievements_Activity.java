package com.example.memerun;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.achievementAdapter;

import java.util.ArrayList;
import java.util.List;

public class achievements_Activity extends AppCompatActivity {

    achievementAdapter adapter;
    ListView listView;

    memeService mService;
    Intent bound;
    boolean mBound = false;
    List<achievement> achievements_list;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.achievements, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.collection:

                Intent collectionIntent = new Intent(achievements_Activity.this, collection_Activity.class);
                startActivityForResult(collectionIntent, COLLECTIONACTIVITY);
                return true;

            case R.id.recent_activity:

                Intent recentIntent = new Intent(achievements_Activity.this, recent_Activity.class);
                startActivityForResult(recentIntent, RECENTACTIVITY);
                return true;

            case R.id.startandstop:

                Intent startAndStopIntent = new Intent(achievements_Activity.this, startAndStop_Activity.class);
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

    List<achievement> tempList;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            achievements_list = mService.getAchievements();

            if(message == "achievements___")
            {
                tempList = new ArrayList<>();

                for(int i = 0; i < 5; ++i)
                {
                    tempList.add(achievements_list.get(i));
                    tempList.get(i).setImageName("question");
                    adapter.add(tempList.get(i));
                }

                int countIsUnlocked = 0;

                if (achievements_list.get(0).isUnlocked()) {

                    for (int i = 0; i < achievements_list.size(); ++i)
                    {
                        if (achievements_list.get(i).isUnlocked())
                        {
                            ++countIsUnlocked;
                        }
                    }
                    adapter.clear();

                    for (int i = 0; i < countIsUnlocked; ++i) {
                        achievements_list.get(i).setBm(getBitmapByNumber(i));
                        adapter.add(achievements_list.get(i));
                    }

                    for(int i = countIsUnlocked; i < tempList.size(); ++i)
                    {
                        adapter.add(tempList.get(i));
                    }
                }
            }

        }


    };

    Bitmap getBitmapByNumber(int position)
    {
        List<bitmapCounter> tempBitmapList = mService.getBmList();
        for(int i = 0; i < tempBitmapList.size(); ++i)
        {
            if(tempBitmapList.get(i).getNumberOfBitmap() == position)
            {
                return tempBitmapList.get(i).getBm();
            }
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_);

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        listView = (ListView) findViewById(R.id.Achievements_listView);
        adapter = new achievementAdapter(this);
        listView.setAdapter(adapter);


        sendInitMessage();

        //achievement user = new achievement("sad_rage");

        Button back = findViewById(R.id.cardview_save);

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
        Log.d("sender", "achievement_started");
        Intent intent = new Intent("memeService");
        intent.putExtra("message", "achievement_init");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_LONG).show();
        unbindService(mConnection);

    }
}
