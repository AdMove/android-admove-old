package com.admove.android.utils;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.admove.android.database.DBHelper;
import com.admove.android.model.Location;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;

import java.util.List;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                AWSMobileClient.defaultMobileClient().getPushManager().registerDevice();
            }
        }).start();

        dbHelper = new DBHelper(this);
    }

    public static DBHelper getDBHelper() {
        return dbHelper;
    }

}
