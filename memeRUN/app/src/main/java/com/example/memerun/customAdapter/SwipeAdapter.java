package com.example.memerun.customAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.memerun.classes.fragmentPage;

public class SwipeAdapter extends FragmentStatePagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

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
}
