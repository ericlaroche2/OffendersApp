package com.example.ericlaroche.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Eric Laroche on 6/15/2017.
 */

public class FeedReaderContract {

    public static class FeedEntry implements BaseColumns {
        //Offender
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_Name = "FullName";
        public static final String COLUMN_NAME_LastName = "LastName";
        public static final String COLUMN_NAME_Location = "Location";
        public static final String COLUMN_NAME_IDs = "ID";
        public static final String COLUMN_NAME_Date = "Date";
        //In/Out
        public static final String TABLE_InOut = "TABLE_InOut";
        public static final String InOut_ID = "InOutID";
        public static final String InOut_OID = "OID";
        public static final String InOut_Direction = "Direction";
        public static final String InOut_Reason = "Reason";
        public static final String InOut_Comment = "Comment";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_Name + " TEXT," +
                    FeedEntry.COLUMN_NAME_LastName + " TEXT," +
                    FeedEntry.COLUMN_NAME_Location + " TEXT," +
                    FeedEntry.COLUMN_NAME_IDs + " TEXT," +
                    FeedEntry.COLUMN_NAME_Date + " TEXT)";

    private static final String SQL_CREATE_InOut =
            "CREATE TABLE " +FeedEntry.TABLE_InOut + " (" +
                    FeedEntry.InOut_ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.InOut_OID + " TEXT," +
                    FeedEntry.InOut_Direction + " TEXT," +
                    FeedEntry.InOut_Reason + " TEXT," +
                    FeedEntry.InOut_Comment + " TEXT)" ;


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;




}

