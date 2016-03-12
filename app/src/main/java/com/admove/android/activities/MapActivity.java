package com.admove.android.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.admove.R;
import com.admove.android.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.RoadsApi;
import com.google.maps.model.SnappedPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * This is how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 * <p/>
 * Created by Giorgi on 2/6/2016.
 */
public class MapActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    @SuppressWarnings("unused")
    public static final CameraPosition LOCATION =
            new CameraPosition.Builder().target(new LatLng(41.7167, 44.7833))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.7167, 44.7833), 10));
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        placeRoad();
    }

    private void placeRoad(){
        ArrayList<LatLng> locations = getLocationsList();
        placeLocations(locations, 10, Color.BLUE);


        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCyg7ZAGa3TjVDaNiE2b7k4Hycq9IiXeUs");
        com.google.maps.model.LatLng[] roadArr = new com.google.maps.model.LatLng[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            roadArr[i] = new com.google.maps.model.LatLng(locations.get(i).latitude, locations.get(i).longitude);
        }
        PendingResult<SnappedPoint[]> result = RoadsApi.snapToRoads(context, roadArr);
        result.setCallback(new PendingResult.Callback<SnappedPoint[]>() {
            @Override
            public void onResult(SnappedPoint[] result) {
                PolylineOptions road = new PolylineOptions().width(10).color(Color.RED);
                for (int i = 0; i < result.length; i++) {
                    LatLng p = new LatLng(result[i].location.lat, result[i].location.lng);
                    road.add(p);
                }
                mMap.addPolyline(road);
                Log.d("locationLog", "place real road");
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d("locationLog", "error: " + e);
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 15));
    }

    private ArrayList<LatLng> getLocationsList(){
        ArrayList<LatLng> list = new ArrayList<>();

        list.add(new LatLng(41.727547, 44.813904));
        list.add(new LatLng(41.727455, 44.815041));
        list.add(new LatLng(41.727479, 44.815824));
        list.add(new LatLng(41.727191, 44.817208));
        list.add(new LatLng(41.726835, 44.817267));
        list.add(new LatLng(41.726807, 44.816006));
        list.add(new LatLng(41.727147, 44.812235));

        return list;
    }



    private void placeLocations(List<LatLng> locations, int width, int color){
        PolylineOptions road = new PolylineOptions().width(width).color(color);
        for(int i = 0 ; i < locations.size() ; i++)
        {
            road.add(locations.get(i));
        }
        mMap.addPolyline(road);
    }


    private void newLocation(Location location){
        Log.d("locationLog", "new location: "+location);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    newLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.d("locationLog", "location status changed");
                }

                public void onProviderEnabled(String provider) {
                    Log.d("locationLog", "location provider enabled: " + provider);
                }

                public void onProviderDisabled(String provider) {
                    Log.d("locationLog", "location provider disabled: " + provider);
                }
            };

// Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

}
