package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthquakeData>> {
    private String url;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<EarthquakeData> loadInBackground() {
        ArrayList<EarthquakeData> earthquakes = null;

        if (url == null) {
            return null;
        }
        earthquakes = Utils.fetchEarthquakeData(url);
        Log.v("TAG6", "HERE: "+earthquakes);
        return earthquakes;
    }

}
