package com.example.memerun.classes;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class bitmapCounter
{
    int numberOfBitmap;
    Bitmap bm;

    public int getNumberOfBitmap() {
        return numberOfBitmap;
    }

    public bitmapCounter(Bitmap bm, int position)
    {
        this.numberOfBitmap = position;
        this.bm = bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBm() {
        return bm;
    }
}
