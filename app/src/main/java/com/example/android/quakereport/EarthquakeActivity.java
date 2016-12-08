/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public static final String USGS_QUERY_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-12-01&endtime=2016-12-06&minmagnitude=5";

    public ArrayList<Quake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_list);



        // Create a fake list of earthquakes
        //final ArrayList<Quake> earthquakes = QueryUtils.extractEarthquakeFeatures();


        // Find a reference to the {@link ListView} in the layout
        // final ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        // ArrayAdapter<Quake> adapter = new QuakeAdapter(
        //        this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        // earthquakeListView.setAdapter(adapter);



        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute();

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Set onclick listener
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the quake object at the given position the user clicked on
                Quake quake = earthquakes.get(position);

                String url = quake.getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void updateUi(ArrayList<Quake> earthquakes) {
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create new ArrayAdapter of earthquakes
        ArrayAdapter<Quake> adapter = new QuakeAdapter(getApplicationContext(), earthquakes);
        earthquakeListView.setAdapter(adapter);
    }

    private class EarthquakeAsyncTask extends AsyncTask<URL, Void, ArrayList<Quake>> {

        @Override
        protected ArrayList<Quake> doInBackground(URL... urls) {
            // Create URL object
            URL url = QueryUtils.createURL(USGS_QUERY_URL);
            // Then perform HTTP request with url object
            String JSONResponse = "";
                    try {
                        JSONResponse = QueryUtils.makeHttpRequest(url);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error creating HTTP request: " + e);
                    }
            // Then extract relevant information from the response

            earthquakes = QueryUtils.extractFeaturesFromJson(JSONResponse);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(ArrayList<Quake> earthquakes) {
            if (earthquakes == null) {
                return;
            }
            updateUi(earthquakes);
        }


    }
}
