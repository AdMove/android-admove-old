package com.admove.android.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admove.R;
import com.admove.android.database.DBFactory;
import com.admove.android.model.Location;
import com.admove.android.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment that appears in the "content_frame", shows home screen
 * <p/>
 * Created by Giorgi on 2/4/2016.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

//    @InjectView(R.id.fragment_text)
    TextView distanceView;

    public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        distanceView = ButterKnife.findById(rootView, R.id.fragment_text);
        new AsyncTask<Void, Void, Double>() {

            @Override
            protected Double doInBackground(Void... params) {
                List<Location> locations = DBFactory.getInstance().getLocationManager().getAll();
                if (locations.size() == 0) {
                    locations = getLocationsList();
                }
                return MapUtils.countDistance(locations.toArray(new Location[0]));
            }

            @Override
            protected void onPostExecute(Double distance) {
                Log.d("snapToRoads", "road distance: " + distance);
                distanceView.setText(String.format("%.2f km", distance));
            }
        }.execute();

//        distance.setText();
        return rootView;
    }

    private ArrayList<Location> getLocationsList() {
        ArrayList<Location> list = new ArrayList<>();

        list.add(new Location(41.727547, 44.813904, 0));
        list.add(new Location(41.727455, 44.815041, 0));
        list.add(new Location(41.727479, 44.815824, 0));
        list.add(new Location(41.727191, 44.817208, 0));
        list.add(new Location(41.726835, 44.817267, 0));
        list.add(new Location(41.726807, 44.816006, 0));
        list.add(new Location(41.727147, 44.812235, 0));

        return list;
    }

}
