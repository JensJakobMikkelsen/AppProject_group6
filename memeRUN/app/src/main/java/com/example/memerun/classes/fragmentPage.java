package com.example.memerun.classes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.memerun.R;

public class fragmentPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        final View view;
        Bundle bundle =getArguments();
        int pageNumber= bundle.getInt("pageNumber");
        view = inflater.inflate(R.layout.page_fragmet_layout,container,false);
        TextView textView = (TextView)view.findViewById(R.id.textviewer_Page1);
        textView.setText(Integer.toString(pageNumber));
        return view;

    }
}
