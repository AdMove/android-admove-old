package com.admove.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admove.R;

/**
 * Fragment that appears in the "content_frame", shows inbox
 * <p/>
 * Created by Giorgi on 2/4/2016.
 */
public class InboxFragment extends android.support.v4.app.Fragment {

    public InboxFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        getActivity().setTitle("Inbox");
        return rootView;
    }

}
