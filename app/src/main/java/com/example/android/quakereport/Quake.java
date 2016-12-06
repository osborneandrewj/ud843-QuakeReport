package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zark on 12/4/2016.
 */

public class Quake {

    private String mPlace;
    private double mMag;
    private long mDate = 0;
    private static final String LOCATION_SPLITTER = " of ";

    /**
     * Create a new Quake object
     *
     * @param aPlace
     * @param aMag
     * @param aTemporaryTimeHolder needs to be replaced
     */
    public Quake(String aPlace, double aMag, long aTemporaryTimeHolder) {
        mPlace = aPlace;
        mMag = aMag;
        mDate = aTemporaryTimeHolder;

    }

    public String getPlace() {
         String[] split = mPlace.split("(?<=of)");
        if (split.length > 1) {
            return split[1];
        } else return "Somewhere";
    }

    public String getNear() {
        String[] split = mPlace.split("(?<=of)");
        return split[0];
    }

    public String getMag() {
        return Double.toString(mMag);
    }

    public String getDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        //Date date = new Date();
        //date.setTime(mDate);

        return dateFormat.format(mDate);
    }

    public String getTime() {

        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm a");

        return timeFormat.format(mDate);
    }


}
