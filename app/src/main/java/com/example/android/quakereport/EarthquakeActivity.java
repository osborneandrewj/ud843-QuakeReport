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

import android.support.v4.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class EarthquakeActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Quake>> {

    /** Adapter for the list of earthquakes */
    private QuakeAdapter mQuakeAdapter;
    /** Log tag */
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_list);

        // Find a reference to the ListView in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list (for now) of earthquakes as input
        mQuakeAdapter = new QuakeAdapter(getApplicationContext(), new ArrayList<Quake>());

        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mQuakeAdapter);

        // Set onclick listener
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the quake object at the given position the user clicked on
                Quake quake = mQuakeAdapter.getItem(position);

                String url = quake.getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
        Log.v(LOG_TAG, "getSupportLoaderManager().initLoader executing!");

        mEmptyStateTextView = (TextView)findViewById(R.id.empty);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
    }


    @Override
    public android.support.v4.content.Loader<ArrayList<Quake>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader here");
        return new EarthquakeLoader(this, "nothing");

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Quake>> loader) {

        // Loader reset, so we can clear out our existing code
        mQuakeAdapter.clear();
        Log.v(LOG_TAG, "onLoaderReset here");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Quake>> loader, ArrayList<Quake> earthquakes) {
        // Clear the adapter of previous earthquake data
        mQuakeAdapter.clear();

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty_text);

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mQuakeAdapter.addAll(earthquakes);
            Log.v(LOG_TAG, "onLoadFinished here");
        }

    }
}
