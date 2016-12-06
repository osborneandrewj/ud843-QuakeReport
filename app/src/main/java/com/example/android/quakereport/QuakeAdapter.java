package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zark on 12/4/2016.
 */

public class QuakeAdapter extends ArrayAdapter<Quake> {

    public QuakeAdapter(Context context, ArrayList<Quake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the inflator and inflate the xml
        View quakeItem = convertView;
        if (quakeItem == null) {
            quakeItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.quake_item, parent, false);

        }

        // Get the object we are displaying
        Quake currentQuake = getItem(position);


        // Get each TextView and set the text
        TextView mag = (TextView)quakeItem.findViewById(R.id.mag_text);
        mag.setText(currentQuake.getMag());
        TextView near = (TextView)quakeItem.findViewById(R.id.direction_text);
        near.setText(currentQuake.getNear());
        TextView place = (TextView)quakeItem.findViewById(R.id.place_text);
        place.setText(currentQuake.getPlace());
        TextView date = (TextView)quakeItem.findViewById(R.id.date_text);
        date.setText(currentQuake.getDate());
        TextView time = (TextView)quakeItem.findViewById(R.id.time_text);
        time.setText(currentQuake.getTime());

        return quakeItem;
    }
}
