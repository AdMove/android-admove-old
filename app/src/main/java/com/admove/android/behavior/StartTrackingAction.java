package com.admove.android.behavior;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.admove.android.services.FusedLocationService;
import com.admove.android.services.LocationTrackingService;

public class StartTrackingAction implements TrackingAction {

    @Override
    public void onClick(View view, Context context) {
        ServiceStatus.set(PreferenceManager.getDefaultSharedPreferences(context), context, true);
        Snackbar.make(view, "Starting to track your movement", Snackbar.LENGTH_LONG)
                .setAction("TrackingAction", null).show();
        Intent intent = new Intent(context, FusedLocationService.class);
        context.startService(intent);
    }

}
