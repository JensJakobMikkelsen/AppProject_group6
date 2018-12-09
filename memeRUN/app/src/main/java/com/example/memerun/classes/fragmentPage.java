package com.example.memerun.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memerun.R;

public class fragmentPage extends Fragment {

    // https://stackoverflow.com/questions/10120119/how-does-the-getview-method-work-when-creating-your-own-custom-adapter
    // https://stackoverflow.com/questions/52359709/newinstance-recieves-null-in-the-fragment

    ImageView img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        Bitmap bmp = null;
        Bundle bundle = getArguments();
        View view = null;

        try
        {
            byte[] byteArray = bundle.getByteArray("image");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        catch(NullPointerException nfe)
        {

        }

        if(bmp != null)
        {
            view = inflater.inflate(R.layout.page_fragmet_layout, container, false);
            img = view.findViewById(R.id.imgviewer_page1);
            img.setImageBitmap(bmp);
        }

        return view;

    }

    public ImageView getImg() {
        return img;
    }
}
