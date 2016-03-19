package com.admove.android.behavior;

import android.content.Context;
import android.content.SharedPreferences;

import com.admove.R;

/**
 * Enumeration that contains possible statuses of location tracking service
 * <p/>
 * Created by Giorgi on 3/19/2016.
 */
public enum ServiceStatus {
    RUNNING {
        @Override
        public String toString() {
            return "Running";
        }
    },
    STOPPED {
        @Override
        public String toString() {
            return "Stopped";
        }
    };

    public static ServiceStatus get(SharedPreferences sharedPreferences, Context context) {
        return sharedPreferences.getBoolean(
                context.getString(R.string.location_listener_status), false) ? RUNNING : STOPPED;
    }

    public static void set(SharedPreferences sharedPreferences, Context context, boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.location_listener_status), status);
        editor.apply();
    }
}
