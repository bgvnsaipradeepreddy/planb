package com.user.planb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 1/20/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "planb.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQliteTables.SQL_CREATE_STATES);
        sqLiteDatabase.execSQL(SQliteTables.SQL_CREATE_DISTRICTS);
        sqLiteDatabase.execSQL(SQliteTables.SQL_CREATE_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQliteTables.SQL_DELETE_STATES);
        sqLiteDatabase.execSQL(SQliteTables.SQL_DELETE_DISTRICTS);
        sqLiteDatabase.execSQL(SQliteTables.SQL_DELETE_PLACES);
        onCreate(sqLiteDatabase);
    }
}
