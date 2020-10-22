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
package com.example.quakereport;

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

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link EarthquakeData} object to represent a single earthquake.
     */
    public static ArrayList<EarthquakeData> fetchEarthquakeData(String requestUrl) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<EarthquakeData> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
//        Log.v("TAG2", "Fetch length: "+earthquakes.size());
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link EarthquakeData} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static ArrayList<EarthquakeData> extractFeatureFromJson(String earthquakeJSON) {
        ArrayList<EarthquakeData> earthquakes = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Try to parse the earthquakeJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(earthquakeJSON);
            JSONArray features = root.getJSONArray("features");

            for(int i =0; i<features.length(); i++){
                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                double magnitude = 0.0;
                long timeInMilliSeconds = properties.getLong("time");
                String location         = properties.getString("place");
                String url              = properties.getString("url");

                String[] locations      = extractString(location);
                String distance         = locations[0];
                location                = locations[1];

                //Checks if magnitude is null or not
                if(!properties.getString("mag").equals("null")){
                    magnitude = properties.getDouble("mag");
                }


                //Log.v("TAG2", "\nMag: "+magnitude +"\nDistance: "+distance+"\nLocation: " +location+"\nDate: "+timeInMilliSeconds);

                // Returns an object of EarthquakeData with extracted data
                earthquakes.add( new EarthquakeData(magnitude, distance, location, timeInMilliSeconds, url));
            }
            Log.v("TAG2", earthquakes.size()+"");
            return earthquakes;

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("TAG", "Problem parsing the earthquake JSON results", e);
        } catch(Exception e){
            return earthquakes;
        }

        return null;
    }

    public static String[] extractString(String location){
        String[] locations = new String[10];
        String distance = " ";
        locations = location.split("of");
        Log.v("TAG3", locations[0]);
        if (locations.length==1){
            location = locations[0];
            distance = "Near the";
        }
        else{
            location = locations[1].substring(1);
            distance = locations[0];
        }
        String[] temp = {distance, location};

        return temp;
    }
}
