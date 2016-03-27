package com.admove.android.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;

public class Location {

    private double latitude;
    private double longitude;
    private long time;

    public Location(double latitude, double longitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public Location(android.location.Location loc) {
        this(loc.getLatitude(), loc.getLongitude(), loc.getTime());
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public LatLng toLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    @Override
    public String toString() {
        return latitude + ":" + longitude + "(" + new Time(time) + ")";
    }
}
