package com.ben.permissions;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;


/**
 * Created by ben on 5/19/2017.
 */
/* Modified from CS 496 Module 7 material on permissions */
class SQLiteLocation extends SQLiteOpenHelper {

    private SQLiteDatabase mSQLDB;

    Context c;

    public SQLiteLocation(Context context) {
        super(context, DBContract.LocationTable.DB_NAME, null, DBContract.LocationTable.DB_VERSION);
        /* DATABASE STUFF */
        mSQLDB = this.getWritableDatabase();
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.LocationTable.SQL_CREATE_DEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.LocationTable.SQL_DROP_DEMO_TABLE);
        onCreate(db);
    }

    public void show() {
        if (mSQLDB != null) {
            try {
                Cursor mSQLCursor = mSQLDB.query(DBContract.LocationTable.TABLE_NAME,
                        new String[]{DBContract.LocationTable._ID,
                                DBContract.LocationTable.COLUMN_NAME_LATITUDE,
                                DBContract.LocationTable.COLUMN_NAME_LONGITUDE,
                                DBContract.LocationTable.COLUMN_NAME_TEXT},
                        null, null, null, null, null);

                /*DEBUG*/
                int cols = mSQLCursor.getColumnCount();
                int rows = mSQLCursor.getCount();
                Log.i("**TEST**", "In record: cols: " + String.valueOf(cols) +
                        ", rows: " + String.valueOf(rows));

                ListView SQLListView = (ListView) ((Activity) this.c).findViewById(R.id.sql_list_view);
                SimpleCursorAdapter mSQLCursorAdapter = new SimpleCursorAdapter(this.c,
                        R.layout.list_item,
                        mSQLCursor,
                        new String[]{DBContract.LocationTable.COLUMN_NAME_LATITUDE,
                                DBContract.LocationTable.COLUMN_NAME_LONGITUDE,
                                DBContract.LocationTable.COLUMN_NAME_TEXT},
                        new int[]{R.id.sql_listview_lat, R.id.sql_listview_lon, R.id.sql_listview_text},
                        0);
                SQLListView.setAdapter(mSQLCursorAdapter);
            } catch (Exception e) {
                Log.e("**TEST**", "in show: " + e.getMessage().toString());
            }
        }
    }

    public void record(String text, Double latitude, Double longitude) {
        if (mSQLDB != null) {
            try {
                // TODO: change this to reflect the way the project database should be set up
                ContentValues values = new ContentValues();
                values.put(DBContract.LocationTable.COLUMN_NAME_LATITUDE, latitude);
                values.put(DBContract.LocationTable.COLUMN_NAME_LONGITUDE, longitude);
                values.put(DBContract.LocationTable.COLUMN_NAME_TEXT, text);
                mSQLDB.insert(DBContract.LocationTable.TABLE_NAME, null, values);
            } catch (Exception e) {
                Log.e("**TEST**", "in record: " + e.getMessage().toString());
            }
        }
    }
}
