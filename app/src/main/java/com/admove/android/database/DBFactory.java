package com.admove.android.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.admove.android.model.Location;
import com.admove.android.utils.App;
import com.admove.android.utils.Function;

import java.util.List;

public class DBFactory {

    private static final DBFactory instance = new DBFactory();

    public static DBFactory getInstance() {
        return instance;
    }

    private DBFactory() {

    }

    public Manager<Location> getLocationManager() {
        return new LocationManager(App.getDBHelper());
    }

}
