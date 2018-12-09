package com.example.memerun.customAdapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.classes.fragmentPage;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    // https://stackoverflow.com/questions/10120119/how-does-the-getview-method-work-when-creating-your-own-custom-adapter

    List<bitmapCounter> tempBitmapList;
    public Bitmap bm;
    int position;
    fragmentPage pageFragment;
    Bitmap notUnlocked;
    List<achievement> achievementList;


    public SwipeAdapter(FragmentManager fm, List<bitmapCounter> tempBitmapList, List<achievement> achievements_, Bitmap not_unlocked_)
    {
        super(fm);
        this.notUnlocked = not_unlocked_;
        this.achievementList = achievements_;
        this.tempBitmapList = tempBitmapList;
    }

    public SwipeAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        this.position = position;
        pageFragment = new fragmentPage();
        Bundle bundle = new Bundle();
        Bitmap bmp = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int size = achievementList.size();

        try
        {
            if(position < size) {
                if (achievementList.get(position).isUnlocked()) {
                    bmp = getBitmapByNumber(position);
                }

                else
                {
                    bmp = notUnlocked;
                }
            }

        }
        catch(NullPointerException nfe)
        {
        }

        //https://stackoverflow.com/questions/50181700/android-bitmap-compression-is-causing-out-of-memory-problems?noredirect=1&lq=1

        if(bmp != null) {

            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bundle.putByteArray("image",byteArray);
            pageFragment.setArguments(bundle);
        }

        return pageFragment;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Bitmap getBitmapByNumber(int position)
    {
        for (int i = 0; i < tempBitmapList.size(); ++i)
        {
            if (tempBitmapList.get(i).getNumberOfBitmap() == position) {
                bm = tempBitmapList.get(i).getBm();
                return bm;


            }
        }
        return null;
    }

    public Bitmap getBm() {
        return bm;
    }

    public List<bitmapCounter> getTempBitmapList() {
        return tempBitmapList;
    }

    public int getPosition() {
        return position;
    }

}
