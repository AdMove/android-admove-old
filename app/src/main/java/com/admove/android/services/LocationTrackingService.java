package com.admove.android.services;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.admove.android.database.DBFactory;

/**
 * Service which tracks user movement in background. This service might stop!
 * The Android system will force-stop a service only when memory is low and
 * it must recover system resources for the activity that has user focus.
 * <p/>
 * Created by Giorgi on 3/13/2016.
 */
public class LocationTrackingService extends Service {
    private String name = "LocationTrackingService";
    private Looper mServiceLooper;

    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("LocationTrackingService",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Location service starting!", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        startForeground(1234, builder.getNotification());

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Location service destroyed!", Toast.LENGTH_SHORT).show();
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

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler implements LocationListener {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Acquire ProviderEnabled reference to the system Location Manager
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            if (checkAccessFineLocationPermission())
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        @Override
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            Log.d(name, "Latitude " + location.getLatitude());
            Log.d(name, "Longitude " + location.getLongitude());
            DBFactory.getInstance().getLocationManager().save(new com.admove.android.model.Location(location));
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

}
