package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zark on 12/5/2016.
 */

public final class QueryUtils {

    /**
     * Used for log tags
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a QueryUtils object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of Earthquake objects that has been built up from parsing
     * a JSON response.
     */
    public static ArrayList<Quake> extractEarthquakeFeatures(String JSONResponse) {

        // Create an empty ArrayList to which we can start adding Quakes
        ArrayList<Quake> earthquakes = new ArrayList<>();

        try {

            // Convert SAMPLE_JSON_OBJECT String into a JSONObject
            JSONObject jsonObject = new JSONObject(JSONResponse);

            // Extract "features" JSONArray
            JSONArray featureArray = jsonObject.getJSONArray("features");

            // Loop through each feature in the featureArray
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentQuake = featureArray.getJSONObject(i);

                // For each feature, get the properties object
                JSONObject currentFeatures = currentQuake.getJSONObject("properties");

                // Extract values of interest
                double mag = currentFeatures.getDouble("mag");
                String place = currentFeatures.getString("place");
                long time = currentFeatures.getLong("time");
                String url = currentFeatures.getString("url");

                // Add values to earthquakes ArrayList
                Quake quakeToBeAdded = new Quake(place, mag, time, url);
                earthquakes.add(quakeToBeAdded);
            }
        } catch (JSONException e) {
            // IF an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    /**
     * Method to create a new Quake object with data parsed from the JSONResponse
     *
     * @param JSONResponse
     * @return (if successful), a new Quake object
     */
    public static ArrayList<Quake> extractFeaturesFromJson(String JSONResponse) {
        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }

        // Create an empty ArrayList to which we can add Quake objects
        ArrayList<Quake> earthquakes = new ArrayList<>();

        try {
            // Convert String into a JSONObject
            JSONObject jsonObject = new JSONObject(JSONResponse);
            // Get an array of returned earthquake events
            JSONArray featureArray = jsonObject.getJSONArray("features");

            for (int i=0; i < featureArray.length(); i++) {
                // For each earthquake event, get the properties object and extract info
                JSONObject currentQuake = featureArray.getJSONObject(i);
                JSONObject currentFeatures = currentQuake.getJSONObject("properties");
                // Extract values of interest
                double mag = currentFeatures.getDouble("mag");
                String place = currentFeatures.getString("place");
                long time = currentFeatures.getLong("time");
                String url = currentFeatures.getString("url");

                earthquakes.add(new Quake(place, mag, time, url));
            }
            // When all earthquake events have been added, return the array
            return earthquakes;

        } catch (JSONException e) {
            // IF an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results: ", e);
        }
        return null;
    }

    /**
     * Method to create a URL object from a string
     *
     * @param aUrlString
     * @return
     */
    public static URL createURL(String aUrlString) {
        URL url = null;
        try {
            url = new URL(aUrlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL: " + e);
        }
        return url;
    }

    /**
     * Method to make an HTTP request from the provided URL object
     *
     * @param url
     * @return
     */
    public static String makeHttpRequest(URL url) throws IOException {

        String JSONresponse = "";

        // If the URL is empty then return early instead of crashing the program
        if (url == null) {
            return JSONresponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();

            // If the connection is successful, get the incoming data
            if (httpURLConnection.getResponseCode() == 200) {
                // Get the data and store it in the inputStream Object
                inputStream = httpURLConnection.getInputStream();
                JSONresponse = readFromStream(inputStream);
                // // TODO: 12/7/2016 Get this parsed into a String
            } else {
                JSONresponse = "";
                Log.v(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating URL connection: " + e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return JSONresponse;
    }

    /**
     * Method to take an InputStream object and convert it into a String
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output  = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = reader.readLine();
            while (line != null) {
                Log.v(LOG_TAG, "Output: " + output + line);
                output.append(line);
                line = reader.readLine();
                Log.v(LOG_TAG, "Output: " + output + line);
            }
        }
        return output.toString();

    }


}
