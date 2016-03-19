package com.admove.android.utils;

import android.app.Application;

import com.admove.android.database.DBHelper;

/**
 * Created by toka on 3/19/2016.
 */
public class App extends Application {

    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new DBHelper(this);
    }

    public static DBHelper getDBHelper(){
        return dbHelper;
    }
}
