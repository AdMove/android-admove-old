package com.admove.android.behavior;

import android.content.Context;
import android.view.View;

/**
 * Implementation of this interface will decide
 * what happens to tracking service when user
 * presses on start or stop button.
 * <p/>
 * Created by Giorgi on 3/19/2016.
 */
public interface TrackingAction {

    void onClick(View view, Context context);

}
