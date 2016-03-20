package com.admove.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.admove.android.utils.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toka on 3/19/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ADMOVE_DB";

    public static final String LOCATION_TABLE_NAME = "LocationTable";
    public static final String LOCATION_LATITUDE_FIELD_NAME = "latitude";
    public static final String LOCATION_LONGITUDE_FIELD_NAME = "longitude";
    public static final String LOCATION_TIME_FIELD_NAME = "time";

    public static int CURRENT_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + LOCATION_TABLE_NAME +
                        "(id integer primary key, " +
                        LOCATION_LATITUDE_FIELD_NAME + " real, " +
                        LOCATION_LONGITUDE_FIELD_NAME + " real, " +
                        LOCATION_TIME_FIELD_NAME + " integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
        onCreate(db);
    }

    public long insertInto(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(table, null, values);
    }

    public <T> List<T> getAllFrom(String table, Function<Cursor, T> handler) {
        Log.d("db-log", "get all from " + table);
        List<T> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table + " ORDER BY id ASC LIMIT 50 ", null);

        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            T item = handler.apply(res);
            Log.d("db-log", item.toString());
            list.add(item);
        }

        return list;
    }

}
