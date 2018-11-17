package com.example.memerun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Bitmap bmImage_bm;

    public DownLoadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public Bitmap getBmImage_bm() {
        return bmImage_bm;
    }

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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
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

        bmImage_bm = scaleDown(result, 256, true);

        /*


        byte[] imageAsBytes=null;
        try {
            imageAsBytes = Base64.decode(encodedImage.getBytes());
        } catch (IOException e) {e.printStackTrace();}
*/
        bmImage.setImageBitmap(result);
    }

}