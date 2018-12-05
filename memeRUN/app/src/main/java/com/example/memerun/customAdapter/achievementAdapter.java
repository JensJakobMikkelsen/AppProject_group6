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

    //View updateView;
    //ViewGroup updateParent;

    Context context_;

    public achievementAdapter(Context context) {
        super(context, 0);
        context_ = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        //updateView = convertView;
        //updateParent = parent;

        achievement user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_achievements, parent, false);
        }

        // Lookup view for data population

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

/*
        // Populate the data into the template view using the data object
        date.setText(user.getDate());

        double metres_d = user.getMetres_run();
        String metres_s = "";

        try
        {
            metres_s = Double.toString(metres_d);
        }
        catch(NumberFormatException nfe)
        {

        }

        metres.setText(metres_s);
        // Return the completed view to render on screen
        */
        return convertView;
    }
}