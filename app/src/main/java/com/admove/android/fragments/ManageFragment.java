package com.admove.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admove.R;

/**
 * Fragment that appears in the "content_frame", shows a manage screen
 * <p/>
 * Created by Giorgi on 2/4/2016.
 */
public class ManageFragment extends android.support.v4.app.Fragment {

    public ManageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        getActivity().setTitle("Manage");
        return rootView;
    }

}
