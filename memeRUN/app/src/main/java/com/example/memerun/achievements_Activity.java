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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.memerun.classes.achievement;
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

            if(message == "achievements___") {
                int countIsUnlocked = 0;
                achievements_list = mService.getAchievements();
                List<achievement> tempAdapterList = new ArrayList<>();

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
                        achievements_list.get(i).setImageName("sad_rage");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_);

        bound = new Intent(this, memeService.class);
        bindService(bound, mConnection, Context.BIND_AUTO_CREATE);

        listView = (ListView) findViewById(R.id.Achievements_listView);
        adapter = new achievementAdapter(this);
        listView.setAdapter(adapter);


        tempList = new ArrayList<>();

        tempList.add(new achievement("Run 235083290 metres", 15));
        tempList.add(new achievement("hej", 30));
        tempList.add(new achievement("Hest", 45));
        tempList.add(new achievement("Run 1 meter", 60));
        tempList.add(new achievement("ghbewj09hjew", 70));

        for(int i = 0; i < 5; ++i)
        {
            tempList.get(i).setImageName("question");
            adapter.add(tempList.get(i));
        }

        sendInitMessage();

        //achievement user = new achievement("sad_rage");

        Button back = findViewById(R.id.Achievements_back_btn);

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
}
