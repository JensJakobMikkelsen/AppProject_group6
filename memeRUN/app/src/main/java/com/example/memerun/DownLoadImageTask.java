package com.example.memerun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownLoadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    // https://stackoverflow.com/questions/5776851/load-image-from-url

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    // https://stackoverflow.com/questions/8471226/how-to-resize-image-bitmap-to-a-given-size/8471294

    public static Bitmap scale(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    protected void onPostExecute(Bitmap result) {

        Bitmap bmmap = scale(result, 512, true);

        bmImage.setImageBitmap(bmmap);
    }

}