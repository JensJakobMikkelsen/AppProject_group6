package com.example.memerun.customAdapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;

import com.example.memerun.classes.bitmapCounter;
import com.example.memerun.classes.fragmentPage;

import java.util.List;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    List<bitmapCounter> tempBitmapList;

    public SwipeAdapter(FragmentManager fm, List<bitmapCounter> tempBitmapList) {

        super(fm);
        this.tempBitmapList = tempBitmapList;
    }

    public SwipeAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment pageFragment = new fragmentPage();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bmp", getBitmapByNumber(position+1));
        //bundle.putInt("pageNumber",position+1);
        pageFragment.setArguments(bundle);
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
                return tempBitmapList.get(i).getBm();

                //img.setImageBitmap(tempBitmapList.get(i).getBm());
            }
        }

        return null;
    }
}
