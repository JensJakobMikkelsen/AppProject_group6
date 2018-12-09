package com.example.memerun.customAdapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.memerun.R;
import com.example.memerun.classes.recent;

public class recentAdapter extends ArrayAdapter<recent> {

    // https://stackoverflow.com/questions/10120119/how-does-the-getview-method-work-when-creating-your-own-custom-adapter

    public recentAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        recent user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_recent, parent, false);
        }

        // Lookup view for data population
        TextView date = convertView.findViewById(R.id.date);
        TextView metres = convertView.findViewById(R.id.metres_run);
        TextView achievement_unlocked = convertView.findViewById(R.id.achievements_unlocked_list_txt);
        TextView ach = convertView.findViewById(R.id.achievements_unlocked_list);

        // Populate the data into the template view using the data object
        date.setText(user.getDate());

        double metres_d = user.getSteps();
        String metres_s = "";

        try
        {
            metres_s = Double.toString(metres_d);
        }
        catch(NumberFormatException nfe)
        {

        }

        metres.setText(metres_s);

        if((user.getUnlockedRequirement() != ""))
        {
            achievement_unlocked.setText(user.getUnlockedRequirement());
        }

        else
        {
            achievement_unlocked.setText("");
            ach.setText("");
        }

        // Return the completed view to render on screen
        return convertView;
    }
}