package com.admove.android.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Service which tracks user movement in background. This service might stop!
 * The Android system will force-stop a service only when memory is low and
 * it must recover system resources for the activity that has user focus.
 * <p/>
 * Created by Giorgi on 2/28/2016.
 */
public class LocationTrackingIntentService extends IntentService implements LocationListener {

    private static final String NAME = "LocationTrackingService";
    private static LocationManager locationManager;

    /**
     * Creates an IntentService without need of name string.
     */
    public LocationTrackingIntentService() {
        this(LocationTrackingIntentService.NAME);
    }

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationTrackingIntentService(String name) {
        super(name);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(5000);
            Log.d(LocationTrackingIntentService.NAME, "I'm alive!");
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Register the listener with the Location Manager to receive location updates
        // TODO: locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        // TODO: makeUseOfNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
