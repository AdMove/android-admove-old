package com.admove.android.database;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.admove.android.model.Location;

import org.junit.Test;

import java.util.List;

/**
 * Cool test
 *
 * Created by toka on 3/19/2016.
 */
public class LocationManagerTest extends AndroidTestCase {

    private Manager<Location> manager;
    private Context context;
    private DBHelper helper;
    Location object;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        helper = new DBHelper(context);
        manager = new LocationManager(helper);
        object = new Location(44.555, 55.444, 123456);
    }

    @Override
    protected void tearDown() throws Exception {
        helper.close();
        context.deleteDatabase(DBHelper.DB_NAME);
        super.tearDown();
    }

    @Test
    public void testSaveAndGetAll() {
        manager.save(object);

        List<Location> all = manager.getAll();

        assertEquals(1, all.size());

        Location loc = all.get(0);

        assertEquals(object.getLatitude(), loc.getLatitude());
        assertEquals(object.getLongitude(), loc.getLongitude());
        assertEquals(object.getTime(), loc.getTime());
    }

    @Test
    public void testDoubleSaveAndGetAll() {
        Location loc1 = new Location(12.345, 54.321, 654321);
        Location loc2 = new Location(78.9654, 45.6547, 123456789);
        manager.save(loc1);
        manager.save(loc2);

        List<Location> all = manager.getAll();

        assertEquals(2, all.size());

        Location loc = all.get(0);

        assertEquals(loc1.getLatitude(), loc.getLatitude());
        assertEquals(loc1.getLongitude(), loc.getLongitude());
        assertEquals(loc1.getTime(), loc.getTime());

        loc = all.get(1);
        assertEquals(loc2.getLatitude(), loc.getLatitude());
        assertEquals(loc2.getLongitude(), loc.getLongitude());
        assertEquals(loc2.getTime(), loc.getTime());

    }
}
