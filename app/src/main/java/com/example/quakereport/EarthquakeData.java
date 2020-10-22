package com.example.quakereport;

public class EarthquakeData {

    private double magnitude;
    private String distance;
    private String location;
    private long timeInMilliSeconds;
    private String url;



    public EarthquakeData(double magnitude, String distance, String location, long timeInMilliSeconds, String url){

        this.magnitude = magnitude;
        this.distance = distance;
        this.location = location;
        this.timeInMilliSeconds = timeInMilliSeconds;
        this.url = url;

    }
    public double getMagnitude() { return magnitude; }

    public String getDistance(){ return distance; }

    public String getLocation() {
        return location;
    }

    public long getTimeInMilliSeconds(){ return timeInMilliSeconds; }

    public String getUrl() { return url; }
}
