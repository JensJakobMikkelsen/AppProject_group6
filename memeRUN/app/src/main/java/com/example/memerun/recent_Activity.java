package com.example.memerun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.memerun.classes.recent;
import com.example.memerun.customAdapter.recentAdapter;

public class recent_Activity extends AppCompatActivity {

    recentAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_);

        listView = (ListView) findViewById(R.id.Achievements_back_btn);
        adapter = new recentAdapter(this);
        listView.setAdapter(adapter);

        adapter.add(new recent(15));
        adapter.add(new recent(30));
        adapter.add(new recent(45));

        Button back = findViewById(R.id.Back_recent_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }

        });


    }
}
