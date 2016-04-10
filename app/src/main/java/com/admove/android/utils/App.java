package com.admove.android.utils;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.admove.android.database.DBHelper;
import com.admove.android.model.Location;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;

/**
 * Created by toka on 3/19/2016.
 */
public class App extends MultiDexApplication {

    private final static String LOG_TAG = "AdMove_log";

    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeApplication();
    }

    private void initializeApplication() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

        // Set a listener for changes in push notification state
        PushManager.setPushStateListener(new PushManager.PushStateListener() {
            @Override
            public void onPushStateChange(final PushManager pushManager, boolean isEnabled) {
                Log.d(LOG_TAG, "Push Notifications Enabled = " + isEnabled);
                // ...Put any application-specific push state change logic here...
            }
        });


        dbHelper = new DBHelper(this);
    }

    public static DBHelper getDBHelper() {
        return dbHelper;
    }

    public static double countDistance(Location... coordinates){
        if (coordinates.length < 2){
            return 0;
        }
        double dist = 0;
        Location start = coordinates[0];
        for (int i=1; i<coordinates.length; i++) {
            Location end = coordinates[i];
            dist += distance(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
            start = end;
        }

        return dist;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;

        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
