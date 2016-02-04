package com.admove.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admove.R;

/**
 * Fragment that appears in the "content_frame", shows map
 * <p/>
 * Created by Giorgi on 2/4/2016.
 */
public class MapFragment extends android.support.v4.app.Fragment {

    public MapFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().setTitle("Map");
        return rootView;
    }

}
