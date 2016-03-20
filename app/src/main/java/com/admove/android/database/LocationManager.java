package com.admove.android.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.admove.android.model.Location;
import com.admove.android.utils.App;
import com.admove.android.utils.Function;

import java.util.List;

/**
 * Created by toka on 3/19/2016.
 */
public class LocationManager  implements Manager<Location>{

    private DBHelper helper;
    public LocationManager(DBHelper helper){
        this.helper = helper;
    }


    @Override
    public void save(Location object) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.LOCATION_LATITUDE_FIELD_NAME, object.getLatitude());
        contentValues.put(DBHelper.LOCATION_LONGITUDE_FIELD_NAME, object.getLongitude());
        contentValues.put(DBHelper.LOCATION_TIME_FIELD_NAME, object.getTime());

        helper.insertInto(DBHelper.LOCATION_TABLE_NAME, contentValues);
    }

    @Override
    public List<Location> getLast(int count) {
        return null;
    }

    @Override
    public List<Location> getAll() {
        return helper.getAllFrom(DBHelper.LOCATION_TABLE_NAME, new Function<Cursor, Location>() {
            @Override
            public Location apply(Cursor arg) {
                return new Location(
                        arg.getDouble(arg.getColumnIndex(DBHelper.LOCATION_LATITUDE_FIELD_NAME)),
                        arg.getDouble(arg.getColumnIndex(DBHelper.LOCATION_LONGITUDE_FIELD_NAME)),
                        arg.getLong(arg.getColumnIndex(DBHelper.LOCATION_TIME_FIELD_NAME))
                );
            }
        });
    }
}
