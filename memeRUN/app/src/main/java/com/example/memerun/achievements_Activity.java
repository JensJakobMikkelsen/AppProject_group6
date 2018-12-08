package com.example.memerun;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.customAdapter.achievementAdapter;

import java.util.ArrayList;
import java.util.List;

public class achievements_Activity extends AppCompatActivity {

    achievementAdapter adapter;
    ListView listView;
    Intent bound;
    boolean mBound = false;
    public boolean execute = true;
    boolean stop = false;


    List<achievement> achievements_list;

    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    int STARTANDSTOPACTIVITY = 111;
    int RECENTACTIVITY = 112;
    int ACHIEVEMENTSACTIVITY = 113;
    int COLLECTIONACTIVITY = 114;

    boolean troll_pressed = false;
    public static final String myPreferences = "MyPrefs";
    SharedPreferences sharedPreferences;
    boolean mShowVisible =false;


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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(achievements_Activity.this);
                // set title
                alertDialogBuilder.setTitle("Set steps");

                final EditText input = new EditText (this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                final TextView input_ = new EditText (this);

                alertDialogBuilder.setView(input);


                // set dialog message
                alertDialogBuilder
                        .setMessage("NEJ")
                        .setCancelable(false)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                //Refreshes UI when connected to service
            }

            public void onServiceDisconnected(ComponentName className) {

                mService = null;
            }
        };
    }
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
                adapter.clear();
                tempList = new ArrayList<>();

                for(int i = 0; i < achievements_list.size(); ++i)
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

            else if (message.equals("Achievement unlocked!")) {

                if(execute) {

                    execute = false;

                    //https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android
                    //https://stackoverflow.com/questions/3263736/playing-a-video-in-videoview-in-android

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(achievements_Activity.this);
                    LayoutInflater inflater = achievements_Activity.this.getLayoutInflater();
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

    private void saveToExternalStorage(Bitmap bmp) {


        // https://developer.android.com/training/permissions/requesting


        /*
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        */

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(achievements_Activity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(achievements_Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "test" , "test");
        }

    }


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

        setupService();

        Intent intent = new Intent(achievements_Activity.this, memeService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;


        listView = (ListView) findViewById(R.id.Achievements_listView);
        adapter = new achievementAdapter(this);
        listView.setAdapter(adapter);

        sendInitMessage();

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        troll_pressed = sharedPreferences.getBoolean("troll_pressed", false);

        if(troll_pressed)
        {
            mShowVisible = true;
            this.invalidateOptionsMenu();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                // https://stackoverflow.com/questions/12244297/how-to-add-multiple-buttons-on-a-single-alertdialog

                AlertDialog.Builder builder = new AlertDialog.Builder(achievements_Activity.this);
                builder.setTitle("Choose:");
                builder.setItems(new CharSequence[]
                                {"Save to phone", "View in full"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:

                                        if(tempList.get(position).isUnlocked()) {
                                            saveToExternalStorage(tempList.get(position).getBm());
                                        }

                                        else
                                        {
                                            int id = getResources().getIdentifier("com.example.memerun:mipmap/question", null, null);
                                            Bitmap bm = BitmapFactory.decodeResource(getResources(), id);
                                            saveToExternalStorage(bm);
                                        }

                                        break;
                                    case 1:
                                        if(achievements_list.get(position).isUnlocked())
                                        {
                                            showImage(tempList.get(position).getBm());
                                        }

                                        else {
                                            Toast.makeText(getApplicationContext(), "Meme not unlocked yet",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        });
                builder.create().show();


            }
        });

        Button back = findViewById(R.id.cardview_save);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();
            }

        });

    }

    Dialog builder;

    // https://stackoverflow.com/questions/7693633/android-image-dialog-popup

    public void showImage(Bitmap bmp) {
        builder = new Dialog(this, android.R.style.Theme_DeviceDefault);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bmp);

        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View View3)
            {
                builder.dismiss();
            }

        });

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("memeService"));
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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
