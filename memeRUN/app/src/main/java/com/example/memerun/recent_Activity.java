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
import android.widget.ListView;

import com.example.memerun.classes.recent;
import com.example.memerun.customAdapter.recentAdapter;

import java.util.List;

public class recent_Activity extends AppCompatActivity {

    recentAdapter adapter;
    ListView listView;

    memeService mService;
    Intent bound;
    boolean mBound = false;

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
            if (message == "recent___")
            {
                adapter.clear();
                List<recent> recentList = mService.getRecentActivity();

                if(recentList.size() != 0)
                {
                    for (int i = 0; i < recentList.size(); ++i) {
                        adapter.add(recentList.get(i));
                    }
                }

                //recentList.clear();

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
