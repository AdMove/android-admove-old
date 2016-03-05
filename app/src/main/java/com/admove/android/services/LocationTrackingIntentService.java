package com.admove.android.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Giorgi on 2/28/2016.
 */
public class LocationTrackingIntentService extends IntentService {

    public LocationTrackingIntentService() {
        this("LocationTrackingIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationTrackingIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
