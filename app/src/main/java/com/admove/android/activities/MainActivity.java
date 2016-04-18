package com.admove.android.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.admove.R;
import com.admove.android.behavior.ServiceStatus;
import com.admove.android.behavior.StartTrackingAction;
import com.admove.android.behavior.StopTrackingAction;
import com.admove.android.behavior.TrackingAction;
import com.admove.android.devauth.demo.LoginActivity;
import com.admove.android.fragments.HomeFragment;
import com.admove.android.fragments.InboxFragment;
import com.admove.android.fragments.ManageFragment;
import com.admove.android.services.FusedLocationService;
import com.admove.android.utils.PushListenerService;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String LOG_TAG = "MainActivity_log";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String DIRECTORY_NAME = "MyCameraApp";

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Received notification from local broadcast. Display it in a dialog.");

            Bundle data = intent.getBundleExtra(PushListenerService.INTENT_SNS_NOTIFICATION_DATA);
            String message = PushListenerService.getMessage(data);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Push Notification")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private Uri fileUri;
    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            private Map<ServiceStatus, TrackingAction> actions =
                    new HashMap<ServiceStatus, TrackingAction>() {{
                        put(ServiceStatus.STOPPED, new StartTrackingAction());
                        put(ServiceStatus.RUNNING, new StopTrackingAction());
                    }};

            @Override
            public void onClick(View view) {
                actions.get(getServiceStatus(FusedLocationService.class))
                        .onClick(view, MainActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();

        View headerView = navigationView.getHeaderView(0);
        TextView userName = (TextView) headerView.findViewById(R.id.nav_user_name);
        userName.setText(identityManager.getUserName());
        ImageView userImage = (ImageView) headerView.findViewById(R.id.nav_user_image);
        Bitmap userBmp = identityManager.getUserImage();
        if (userBmp != null) {
            userImage.setImageBitmap(userBmp);
        }

        changeFragment(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(PushListenerService.ACTION_SNS_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("temp", "option selected: " + item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Close all currently open drawer views by animating them out of view.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.nav_logout) {
            AWSMobileClient.defaultMobileClient().getIdentityManager().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Handle navigation view item clicks here.
            changeFragment(item.getItemId());
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void changeFragment(int id) {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            // Handle the home action
            fragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_inbox) {
            // Handle the inbox action
            fragment = new InboxFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            // Handle the manage action
            fragment = new ManageFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_camera) {
            // Handle the camera action
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_gallery) {
            // Handle the gallery action

        } else if (id == R.id.nav_share) {
            // Handle the share action

        } else if (id == R.id.nav_contact) {
            // Handle the contact action

        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), DIRECTORY_NAME);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(DIRECTORY_NAME, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    private ServiceStatus getServiceStatus(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return ServiceStatus.RUNNING;
            }
        }
        return ServiceStatus.STOPPED;
    }

}
