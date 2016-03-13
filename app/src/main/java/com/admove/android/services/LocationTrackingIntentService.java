package com.admove.android.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Service which tracks user movement in background. This service might stop!
 * The Android system will force-stop a service only when memory is low and
 * it must recover system resources for the activity that has user focus.
 * <p/>
 * Created by Giorgi on 2/28/2016.
 */
public class LocationTrackingIntentService extends IntentService
        implements LocationListener {

    private static String name;

    /**
     * Creates an IntentService without need of name string.
     */
    @SuppressWarnings("unused")
    public LocationTrackingIntentService() {
        this("LocationTrackingService");
    }

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationTrackingIntentService(String name) {
        super(name);
        LocationTrackingIntentService.name = name;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(50000);
            Log.d(name, "I'm alive!");
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        startForeground(1234, builder.getNotification());

        Log.i(name, "Creating location tracking service!");
        // Acquire ProviderEnabled reference to the system Location Manager
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (checkAccessFineLocationPermission())
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(name, "Destroying location tracking service!");
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (checkAccessFineLocationPermission())
            // Remove the listener we previously added
            locationManager.removeUpdates(this);
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean checkAccessFineLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        Log.d(name, "Latitude " + location.getLatitude());
        Log.d(name, "Longitude " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(name, "Status changed!");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(name, "Provider Enabled!");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(name, "Provider Disabled!");
    }

}
