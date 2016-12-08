package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Zark on 12/4/2016.
 */

public class QuakeAdapter extends ArrayAdapter<Quake> {

    DecimalFormat decimalFormat = new DecimalFormat("0.0");

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
        TextView mag = (TextView) quakeItem.findViewById(R.id.mag_text);
        mag.setText(decimalFormat.format(currentQuake.getMag()));

        TextView near = (TextView) quakeItem.findViewById(R.id.direction_text);
        near.setText(currentQuake.getNear());

        TextView place = (TextView) quakeItem.findViewById(R.id.place_text);
        place.setText(currentQuake.getPlace());

        TextView date = (TextView) quakeItem.findViewById(R.id.date_text);
        date.setText(currentQuake.getDate());

        TextView time = (TextView) quakeItem.findViewById(R.id.time_text);
        time.setText(currentQuake.getTime());

        // Set the proper background color on the magnitude circle
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = ContextCompat.getColor(getContext(), getMagnitudeCircleColor(currentQuake.getMag()));

        // Set the background color of the circle
        magnitudeCircle.setColor(magnitudeColor);

        return quakeItem;
    }

    /**
     * Get the color of the circle based on the value of the earthquake's magnitude
     *
     * @param aMagnitude
     * @return
     */
    private int getMagnitudeCircleColor(double aMagnitude) {
        Double magnitudeDouble = aMagnitude;
        int magnitudeFloor = magnitudeDouble.intValue();
        switch (magnitudeFloor) {
            case 0:
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            default:
                return R.color.magnitude10plus;

        }
    }
}
