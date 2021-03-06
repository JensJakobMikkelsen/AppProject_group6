package com.example.memerun.customAdapter;


import android.content.Context;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.memerun.R;
import com.example.memerun.classes.achievement;
import com.example.memerun.classes.recent;

import java.net.URL;

public class achievementAdapter extends ArrayAdapter<achievement> {

    // https://stackoverflow.com/questions/10120119/how-does-the-getview-method-work-when-creating-your-own-custom-adapter

    Context context_;

    public achievementAdapter(Context context) {
        super(context, 0);
        context_ = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        achievement user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_achievements, parent, false);
        }

        int id = context_.getResources().getIdentifier("com.example.memerun:mipmap/" + user.getImageName(), null, null);
        ImageView achievements_image = (ImageView) convertView.findViewById(R.id.Achievements_image);
        if(user.getBm() != null) {
            achievements_image.setImageBitmap(user.getBm());
        }
        else
        {
              achievements_image.setImageResource(id);
        }
        TextView requirement = convertView.findViewById(R.id.requirement_in_m_text);
        requirement.setText(user.getRequirement());


        return convertView;
    }
}