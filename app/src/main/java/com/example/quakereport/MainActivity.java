package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthquakeData>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private QuickSort quickSort = new QuickSort();
    private Button sortList;
    private ListView earthquakeListView;
    private TextView emptyTextView;
    private ProgressBar progressBar;
    private DataAdapter adapter;
    private ArrayList<EarthquakeData> earthquakes = new ArrayList<>();
    private ImageView sortingArrow;
    private boolean sortAscending = true;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-02-01&endtime=2020-02-02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        sortList            = (Button) findViewById(R.id.btnSort);
        sortingArrow        = (ImageView) findViewById(R.id.sorting_arrow);
        earthquakeListView  = (ListView) findViewById(R.id.list);
        progressBar         = (ProgressBar) findViewById(R.id.loading_spinner);
        emptyTextView       = (TextView) findViewById(R.id.empty_text_view) ;

        // Setting Default List View
        earthquakeListView.setEmptyView(progressBar);

        // Create a new {@link DataAdapter} of earthquakes
        adapter = new DataAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = earthquakes.get(i).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        sortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(earthquakes.size()==0){
                    return;
                }
                if (sortAscending) {
                    sortingArrow.setImageResource(R.drawable.ic_arrow_downward_24);
                    quickSort.sort(earthquakes, 0, earthquakes.size() - 1, true);
                    sortAscending = !sortAscending;
                } else {
                    sortingArrow.setImageResource(R.drawable.ic_arrow_upward_24);
                    quickSort.sort(earthquakes, 0, earthquakes.size() - 1, false);
                    sortAscending = !sortAscending;
                }
//                Log.v("TAG5", "" + earthquakes);
                updateUI();
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            getSupportLoaderManager().initLoader(1, null, this).forceLoad();
        }
        else{
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText("No INTERNET Connection");

        }


//        NetworkAsyncTask task = new NetworkAsyncTask();
//        task.execute(USGS_REQUEST_URL);
    }

    private void updateUI() {
        earthquakeListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Loader<ArrayList<EarthquakeData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<EarthquakeData>> loader, ArrayList<EarthquakeData> earthquakes) {
        if (earthquakes == null) {
            Toast.makeText(MainActivity.this, "INVALID URL", Toast.LENGTH_SHORT).show();
        }

        MainActivity.this.earthquakes.clear();
        MainActivity.this.earthquakes.addAll(earthquakes);
        updateUI();
        Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<EarthquakeData>> loader) {

        adapter.clear();

    }

//    private class NetworkAsyncTask extends AsyncTask<String, Void, ArrayList<EarthquakeData>> {
//
//
//        @Override
//        protected ArrayList<EarthquakeData> doInBackground(String... urls) {
//            ArrayList<EarthquakeData> earthquakes = null;
//
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//            earthquakes = Utils.fetchEarthquakeData(urls[0]);
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<EarthquakeData> earthquakes) {
//
//            if (earthquakes == null) {
//                Toast.makeText(MainActivity.this, "INVALID URL", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            MainActivity.this.earthquakes.clear();
//            MainActivity.this.earthquakes.addAll(earthquakes);
//            Log.v("TAG5", "" + earthquakes);
//            Log.v("TAG5", "" + MainActivity.this.earthquakes);
//            updateUI();
//            Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_SHORT).show();
//
//        }
//    }

}