package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.QueryUtils.createUrlString;

/**
 * Created by Zark on 12/9/2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Quake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Create a new loader which
     * @param context
     * @param url
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
        mUrl = createUrlString();
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStart Loading here!");
        forceLoad();
    }

    @Override
    public ArrayList<Quake> loadInBackground() {
        Log.v(LOG_TAG, "Now running loadInBackground");
        if (mUrl == null) {
            return null;
        }
        ArrayList<Quake> earthquakes;

        // Then perform HTTP request with url object
        String JSONResponse = "";
        try {
            JSONResponse = QueryUtils.makeHttpRequest(QueryUtils.createURL(createUrlString()));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating HTTP request: " + e);
        }
        // Then extract relevant information from the response
        earthquakes = QueryUtils.extractFeaturesFromJson(JSONResponse);
        // Return the ArrayList
        return earthquakes;
    }
}
