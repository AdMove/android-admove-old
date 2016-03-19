package com.admove.android.behavior;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.admove.android.services.LocationTrackingService;

/**
 * Created by Giorgi on 3/19/2016.
 */
public class StartTrackingAction implements Action {

    @Override
    public void onClick(View view, Context context) {
        ServiceStatus.set(PreferenceManager.getDefaultSharedPreferences(context), context, true);
        Snackbar.make(view, "Starting to track your movement", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Intent intent = new Intent(context, LocationTrackingService.class);
        context.startService(intent);
    }

}
