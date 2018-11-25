package com.example.memerun.customAdapter;

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

        //Er det den her funktion der arbejdes i? I så fald skulle setBitMapByNumber kaldes her, går jeg ud fra


        Fragment pageFragment = new fragmentPage();
        Bundle bundle = new Bundle();
        bundle.putInt("pageNumber",position+1);
        pageFragment.setArguments(bundle);
        return pageFragment;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    void setBitmapByNumber(int position, ImageView img)
    {
        for (int i = 0; i < tempBitmapList.size(); ++i)
        {
            if (tempBitmapList.get(i).getNumberOfBitmap() == position) {
                img.setImageBitmap(tempBitmapList.get(i).getBm());
            }
        }
    }

}
