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
            final memeURL Modern = new memeURL("https://i.imgur.com/50ZYJZ1.png");
            final memeURL ragecomic = new memeURL("https://i.kym-cdn.com/entries/icons/mobile/000/005/875/RageComics.jpg");
            final memeURL notfunny = new memeURL("https://i.redd.it/t214o6phqgf01.png");
            final memeURL bong = new memeURL("https://pics.me.me/le-empty-bag-o-weed-le-bong-shatter-the-greatest-3910848.png");
            final memeURL sonnyday = new memeURL("http://weknowmemes.com/wp-content/uploads/2012/03/scumbag-sun.jpg");
            final memeURL ghost = new memeURL("https://rg00018.files.wordpress.com/2012/09/rage-comic-i-hear-dead-people.png");
            final memeURL road = new memeURL("https://doilooksick.files.wordpress.com/2017/02/h8631d747.png?w=775&h=685");
            final memeURL dildo = new memeURL("https://i.ytimg.com/vi/c4XZc1fmGl8/maxresdefault.jpg");

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
            mDao.insert(Modern);
            mDao.insert(ragecomic);
            mDao.insert(notfunny);
            mDao.insert(bong);
            mDao.insert(sonnyday);
            mDao.insert(ghost);
            mDao.insert(road);
            mDao.insert(dildo);

            achievement achievement1 = new achievement("5 steps", 5);
            achievement achievement2 = new achievement("50 steps", 50);
            achievement achievement3 = new achievement("100 steps", 100);
            achievement achievement4 = new achievement("200 steps", 200);
            achievement achievement5 = new achievement("400 steps", 400);
            achievement achievement6 = new achievement("650 steps", 650);
            achievement achievement7 = new achievement("950 steps", 950);
            achievement achievement8 = new achievement("1300 steps", 1300);
            achievement achievement9 = new achievement("1650 steps", 1650);
            achievement achievement10 = new achievement("2050 steps", 2050);
            achievement achievement11= new achievement("2500 steps", 2500);
            achievement achievement12 = new achievement("3000 steps", 3000);
            achievement achievement13 = new achievement("3550 steps", 3550);
            achievement achievement14 = new achievement("4150 steps", 4150);
            achievement achievement15= new achievement("4700 steps", 4700);
            achievement achievement16 = new achievement("5550 steps", 5550);
            achievement achievement17 = new achievement("6450 steps", 6450);
            achievement achievement18 = new achievement("7400 steps", 7400);
            achievement achievement19= new achievement("8400 steps", 8400);
            achievement achievement20 = new achievement("9450 steps", 9450);
            achievement achievement21 = new achievement("10500 steps", 10500);
            achievement achievement22 = new achievement("11600 steps", 11600);


            mDao.insert(achievement1);
            mDao.insert(achievement2);
            mDao.insert(achievement3);
            mDao.insert(achievement4);
            mDao.insert(achievement5);
            mDao.insert(achievement6);
            mDao.insert(achievement7);
            mDao.insert(achievement8);
            mDao.insert(achievement9);
            mDao.insert(achievement10);
            mDao.insert(achievement11);
            mDao.insert(achievement12);
            mDao.insert(achievement13);
            mDao.insert(achievement14);
            mDao.insert(achievement15);
            mDao.insert(achievement16);
            mDao.insert(achievement17);
            mDao.insert(achievement18);
            mDao.insert(achievement19);
            mDao.insert(achievement20);
            mDao.insert(achievement21);
            mDao.insert(achievement22);

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

