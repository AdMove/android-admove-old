package com.admove.android.behavior;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.admove.android.services.FusedLocationService;

public class StopTrackingAction implements TrackingAction {

    @Override
    public void onClick(View view, Context context) {
        ServiceStatus.set(PreferenceManager.getDefaultSharedPreferences(context), context, false);
        Snackbar.make(view, "Stopping tracking your movement", Snackbar.LENGTH_LONG)
                .setAction("TrackingAction", null).show();
        Intent intent = new Intent(context, FusedLocationService.class);
        context.stopService(intent);
    }

}
