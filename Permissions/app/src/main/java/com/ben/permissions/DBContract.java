package com.ben.permissions;

import android.provider.BaseColumns;

/**
 * Created by ben on 5/19/2017.
 */
/* Modified from CS 496 Module 7 material on permissions */
final class DBContract {
    private DBContract(){}

    public final class LocationTable implements BaseColumns {
        public static final String DB_NAME = "location_db";
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final int DB_VERSION = 4;


        public static final String SQL_CREATE_DEMO_TABLE = "CREATE TABLE " +
                LocationTable.TABLE_NAME + "(" + LocationTable._ID + " INTEGER PRIMARY KEY NOT NULL," +
                LocationTable.COLUMN_NAME_LATITUDE + " REAL," +
                LocationTable.COLUMN_NAME_LONGITUDE + " REAL," +
                LocationTable.COLUMN_NAME_TEXT + " VARCHAR(255));";

        public  static final String SQL_DROP_DEMO_TABLE = "DROP TABLE IF EXISTS " + LocationTable.TABLE_NAME;
    }
}
