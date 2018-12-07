package com.example.memerun.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.memeURL;
import com.example.memerun.classes.recent;

import java.lang.ref.WeakReference;

@Database(entities = {memeURL.class, achievement.class, recent.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    //Most come from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0

    static final Migration MIGRATION_1_2 = new Migration(1, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public abstract daoAccess daoAccess();
    private static volatile AppDatabase INSTANCE;

    private static WeakReference<Context> contextRef;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {

                    contextRef = new WeakReference<>(context);

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "stockDB")
                            //.addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new PopulateDbAsync(INSTANCE, contextRef.get()).execute();
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE, contextRef.get()).execute();
                }
            };


    //Pre-populate database

    public static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final daoAccess mDao;

        PopulateDbAsync(AppDatabase db, Context context) {
            mDao = db.daoAccess();
            contextRef = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(final Void... params) {

            mDao.deleteAll_achievements();
            mDao.deleteAll_URLs();

            final memeURL yoda = new memeURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpvUWRq0XGg9-27dA-qm1mnp4ksXu1QdUqrvgBZdzdGizmXtef4g");
            final memeURL successkid = new memeURL("http://ubuyfirst.com/wp-content/uploads/2015/01/the-game-i-won-it.jpg");
            final memeURL burrito = new memeURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIKLJiJY_Z_tvxb5S8tWXrfGGKw47LX3y3H-Yy8FwccWzrkeDe");
            final memeURL lion = new memeURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXJd-Jp0hw-nnPhNvTxdas9x4Qx6woACIOrYK5bLjuPvJuWfsptw");
            final memeURL caveman = new memeURL("https://i.ytimg.com/vi/Ugw6Aod27sU/hqdefault.jpg");
            final memeURL goal = new memeURL("https://www.lifewire.com/thmb/hR2GPjS2ZUQBRv2CHCdPcIfBQZ0=/768x0/filters:no_upscale():max_bytes(150000):strip_icc()/sstMxMh-5ab00c7bfa6bcc003622e4f5.jpg");
            final memeURL pain = new memeURL("https://www.todaysparent.com/wp-content/uploads/2017/06/when-your-kid-becomes-a-meme-1024x576-1497986561.jpg");
            final memeURL getThanos = new memeURL("https://img-9gag-fun.9cache.com/photo/aOYzXdM_700bwp.webp");
            final memeURL stairs = new memeURL("https://img-9gag-fun.9cache.com/photo/a6ObY0R_700bwp.webp");
            final memeURL robot = new memeURL("https://img-9gag-fun.9cache.com/photo/aB0WAYZ_460swp.webp");
            final memeURL pikachu = new memeURL("https://i.imgur.com/n2RTfvL.jpg");
            final memeURL yours = new memeURL("https://i.imgur.com/zDKQfzn.jpg");
            final memeURL hotdogs = new memeURL("https://i.imgur.com/EuPLdJY.jpg");
            final memeURL bathroom = new memeURL("https://i.imgur.com/xLJfNDv.jpg");
            mDao.insert(yoda);
            mDao.insert(successkid);
            mDao.insert(burrito);
            mDao.insert(lion);
            mDao.insert(caveman);
            mDao.insert(goal);
            mDao.insert(pain);
            mDao.insert(getThanos);
            mDao.insert(stairs);
            mDao.insert(robot);
            mDao.insert(pikachu);
            mDao.insert(yours);
            mDao.insert(hotdogs);
            mDao.insert(bathroom);

            achievement achievement1 = new achievement("5 steps", 5);
            achievement achievement2 = new achievement("50 steps", 50);
            achievement achievement3 = new achievement("500 steps", 500);
            achievement achievement4 = new achievement("5000 steps", 5000);
            achievement achievement5 = new achievement("50000 steps", 50000);

            mDao.insert(achievement1);
            mDao.insert(achievement2);
            mDao.insert(achievement3);
            mDao.insert(achievement4);
            mDao.insert(achievement5);

            Log.d("sender", "Broadcasting message");
            Intent intent = new Intent("memeService");
            // You can also include some extra data.
            intent.putExtra("message", "databasePopulated");
            LocalBroadcastManager.getInstance(contextRef.get()).sendBroadcast(intent);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
/*
            Log.d("sender", "Broadcasting message");
            Intent intent = new Intent("memeService");
            // You can also include some extra data.
            intent.putExtra("message", "databasePopulated");
            LocalBroadcastManager.getInstance(contextRef.get()).sendBroadcast(intent);
*/
        }

    }

}

