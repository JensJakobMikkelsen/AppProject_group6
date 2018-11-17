package com.example.memerun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.memerun.classes.achievement;
import com.example.memerun.customAdapter.achievementAdapter;

public class achievements_Activity extends AppCompatActivity {

    achievementAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_);

        listView = (ListView) findViewById(R.id.Achievements_listView);
        adapter = new achievementAdapter(this);
        listView.setAdapter(adapter);

        adapter.add(new achievement("sad_rage"));
        adapter.add(new achievement("sad_rage"));


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
}
