package com.example.quakereport;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataAdapter extends ArrayAdapter<EarthquakeData> {
    private ArrayList<EarthquakeData> earthquakes;
    public DataAdapter( Context context, ArrayList<EarthquakeData> earthquakes) {
        super(context, 0, earthquakes);
        this.earthquakes = earthquakes;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_item, parent, false);
        }

        EarthquakeData data = getItem(position);

        long timeInMilliseconds = data.getTimeInMilliSeconds();
        double magnitude = data.getMagnitude();
        Date dateObject = new Date(timeInMilliseconds);
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        String formattedMagnitude = formatMagnitude(magnitude);


        TextView mMagnitude = (TextView) listItemView.findViewById(R.id.tvMagnitude);
        mMagnitude.setText(formattedMagnitude);

        TextView mDistance = (TextView) listItemView.findViewById(R.id.tvDistance);
        mDistance.setText(data.getDistance());

        TextView mLocation = (TextView) listItemView.findViewById(R.id.tvLocation);
        mLocation.setText(data.getLocation());

        TextView mDate = (TextView) listItemView.findViewById(R.id.tvDate);
        mDate.setText(formattedDate);

        TextView mTime = (TextView) listItemView.findViewById(R.id.tvtime);
        mTime.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mMagnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = ContextCompat.getColor(getContext(), getMagnitudeColor(data.getMagnitude()) ) ;

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int color;
        int intMagnitude = (int)magnitude;
        Log.v("TAG4",""+intMagnitude);
        switch (intMagnitude){
            case 10:
                return R.color.magnitude10plus;
            case 9:
                return R.color.magnitude9;
            case 8:
                return R.color.magnitude8;
            case 7:
                return R.color.magnitude7;
            case 6:
                return R.color.magnitude6;
            case 5:
                return R.color.magnitude5;
            case 4:
                return R.color.magnitude4;
            case 3:
                return R.color.magnitude3;
            case 2:
                return R.color.magnitude2;
            case 1:
                return R.color.magnitude1;
            default:
                return R.color.magnitude1;
        }
    }

    // Converts the time in milliseconds into a Date object by calling the Date constructor.
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

}
