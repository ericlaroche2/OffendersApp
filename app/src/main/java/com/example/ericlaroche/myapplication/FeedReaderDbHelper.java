package com.example.ericlaroche.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

import static com.example.ericlaroche.myapplication.FeedReaderContract.FeedEntry.COLUMN_NAME_IDs;

/**
 * Created by Eric Laroche on 6/16/2017.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper implements Serializable {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4585;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_Name + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LastName + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_Location + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_IDs + " TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_Date + " TEXT)";


    private static final String SQL_CREATE_InOut = "CREATE TABLE " +   FeedReaderContract.FeedEntry.TABLE_InOut+ " ("  +
            FeedReaderContract.FeedEntry.InOut_ID + " INTEGER PRIMARY KEY," +
            FeedReaderContract.FeedEntry.InOut_OID + " TEXT," +
            FeedReaderContract.FeedEntry.InOut_Direction + " TEXT," +
            FeedReaderContract.FeedEntry.InOut_Reason + " TEXT," +
            FeedReaderContract.FeedEntry.InOut_Comment + " TEXT," +
            "FOREIGN KEY ("+ FeedReaderContract.FeedEntry.InOut_OID+ ") REFERENCES "+ FeedReaderContract.FeedEntry.TABLE_NAME +" ("+ FeedReaderContract.FeedEntry.COLUMN_NAME_IDs+")"+
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_InOut);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public  String getDatabaseName() {
        return DATABASE_NAME;
    }

}