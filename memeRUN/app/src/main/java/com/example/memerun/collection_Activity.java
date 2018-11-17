package com.example.memerun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class collection_Activity extends AppCompatActivity {

    DownLoadImageTask download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_);

        download = (DownLoadImageTask) new DownLoadImageTask((ImageView) findViewById(R.id.URLview))
        .execute("https://i.imgflip.com/10r5wh.jpg");

        Button back = findViewById(R.id.Back_collection_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }

        });


    }
}
