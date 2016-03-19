package com.admove.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.admove.android.database.DBHelper;
import com.admove.android.model.Location;
import com.admove.android.utils.Function;

import org.junit.Test;

import java.util.List;

/**
 * Created by toka on 3/19/2016.
 */
public class DBTest extends AndroidTestCase {

    private DBHelper helper;
    private Context context;
    Location object;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        helper = new DBHelper(context);
        object = new Location(44.555, 55.444, 123456);
    }

    @Override
    protected void tearDown() throws Exception {
        helper.close();
        context.deleteDatabase(DBHelper.DB_NAME);
        super.tearDown();
    }

    @Test
    public void testAddLocation(){
        ContentValues values = new ContentValues();

        values.put(DBHelper.LOCATION_LATITUDE_FIELD_NAME, object.getLatitude());
        values.put(DBHelper.LOCATION_LONGITUDE_FIELD_NAME, object.getLongitude());
        values.put(DBHelper.LOCATION_TIME_FIELD_NAME, object.getTime());

        long id = helper.insertInto(DBHelper.LOCATION_TABLE_NAME, values);
        assertNotSame(-1, id);
    }

    @Test
    public void testGetAllLocation(){
        ContentValues values = new ContentValues();

        values.put(DBHelper.LOCATION_LATITUDE_FIELD_NAME, object.getLatitude());
        values.put(DBHelper.LOCATION_LONGITUDE_FIELD_NAME, object.getLongitude());
        values.put(DBHelper.LOCATION_TIME_FIELD_NAME, object.getTime());

        long id = helper.insertInto(DBHelper.LOCATION_TABLE_NAME, values);
        assertNotSame(-1, id);

        List<Location> all = helper.getAllFrom(DBHelper.LOCATION_TABLE_NAME, new Function<Cursor, Location>() {
            @Override
            public Location apply(Cursor arg) {
                return new Location(
                        arg.getDouble(arg.getColumnIndex(DBHelper.LOCATION_LATITUDE_FIELD_NAME)),
                        arg.getDouble(arg.getColumnIndex(DBHelper.LOCATION_LONGITUDE_FIELD_NAME)),
                        arg.getLong(arg.getColumnIndex(DBHelper.LOCATION_TIME_FIELD_NAME))
                );
            }
        });

        assertEquals(1, all.size());

        Location loc = all.get(0);

        assertEquals(object.getLatitude(), loc.getLatitude());
        assertEquals(object.getLongitude(), loc.getLongitude());
        assertEquals(object.getTime(), loc.getTime());
    }
}
