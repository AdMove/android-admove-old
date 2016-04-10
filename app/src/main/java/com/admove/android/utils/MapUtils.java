package com.admove.android.utils;

import android.util.Log;

import com.admove.android.model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.SnappedPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toka on 4/10/2016.
 */
public class MapUtils {

    private static final String API_KEY = "AIzaSyCyg7ZAGa3TjVDaNiE2b7k4Hycq9IiXeUs";
    private static GeoApiContext geoApiContext;
    public static GeoApiContext getGeoApiContext(){
        if (geoApiContext == null){
            geoApiContext = new GeoApiContext().setApiKey(API_KEY);
        }
        return geoApiContext;
    }


    public static List<Location> snapToRoads(List<Location> locations) {
        List<Location>[] parts = splitWithSize(locations, 99);
        ArrayList<Location> result = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            List<Location> part = parts[i];
            if (i > 0) {
                part.add(0, result.get(result.size() - 1));
            }
            List<Location> res = _snapToRoads(part, getGeoApiContext());
            if (res == null) {
                return null;
            }
            if (i > 0) {
                res.remove(0);
            }
            result.addAll(res);
        }
        return result;
    }

    public static double countDistance(Location... coordinates) {
        if (coordinates.length < 2) {
            return 0;
        }
        double dist = 0;
        Location start = coordinates[0];
        for (int i = 1; i < coordinates.length; i++) {
            Location end = coordinates[i];
            dist += distance(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
            start = end;
        }

        return dist;
    }


    private static List<Location> _snapToRoads(List<Location> locations, GeoApiContext context) {
        final com.google.maps.model.LatLng[] roadArr = new com.google.maps.model.LatLng[locations.size()];

        for (int i = 0; i < locations.size(); i++) {
            roadArr[i] = new com.google.maps.model.LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
        }
        try {
            SnappedPoint[] snapped = RoadsApi.snapToRoads(context, true, roadArr).await();
            ArrayList<Location> result = new ArrayList<>();
            for (int i = 0; i < snapped.length; i++) {
                result.add(new Location(snapped[i].location.lat, snapped[i].location.lng, 0));
            }
            return result;
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return null;
    }

    private static List<Location>[] splitWithSize(List<Location> list, int maxSize) {
        List<Location>[] result = new List[(list.size() + maxSize - 1) / maxSize];
        for (int i = 0; i < result.length; i++) {
            ArrayList<Location> part = new ArrayList<>();
            for (int j = i * maxSize; j < Math.min(list.size(), (i + 1) * maxSize); j++) {
                part.add(list.get(j));
            }
            result[i] = part;
        }
        return result;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;

        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
