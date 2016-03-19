package com.admove.android.database;

import android.location.Location;

import java.util.List;

public class DBFactory {

    private DBFactory() {

    }

    public static DBFactory getInstance() {
        return new DBFactory();
    }

    public Manager<Location> getLocationManager() {
        return new Manager<Location>() {
            @Override
            public void save(Location object) {

            }

            @Override
            public List<Location> getAll() {
                return null;
            }
        };
    }

}
