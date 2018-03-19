package com.user.planb.database;

/**
 * Created by user on 1/20/2018.
 */

public class SQliteTables {

    /**
     * States table parameters
     */
    public static final String TABLE_STATES = "states";
    public static final String COLUMN_STATE_ID = "state_id";
    public static final String COLUMN_STATE_NAME = "state_name";

    public static final String SQL_CREATE_STATES =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_STATES + "("+
                    COLUMN_STATE_ID + " INTEGER PRIMARY KEY, "+
                    COLUMN_STATE_NAME + " TEXT"+ ");";
    public static final String SQL_DELETE_STATES =
            "DROP TABLE "+TABLE_STATES;

    /**
     * Districts table parameters
     */
    public static final String TABLE_DISTRICTS = "districts";
    public static final String COLUMN_DISTRICTS_ID = "district_id";
    public static final String COLUMN_DISTRICTS_STATE_ID = "district_state_id";
    public static final String COLUMN_DISTRICTS_NAME = "district_name";

    public static final String SQL_CREATE_DISTRICTS =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_DISTRICTS + "("+
                    COLUMN_DISTRICTS_ID + " INTEGER PRIMARY KEY, "+
                    COLUMN_DISTRICTS_STATE_ID + " INTEGER, "+
                    COLUMN_DISTRICTS_NAME + " TEXT"+ ");";
    public static final String SQL_DELETE_DISTRICTS =
            "DROP TABLE "+TABLE_DISTRICTS;

    /*
    Places table parameters
     */
    public static final String TABLE_PLACES = "places";
    public static final String COLUMN_PLACES_ID = "place_id";
    public static final String COLUMN_PLACES_DISTRICTS_ID = "place_district_id";
    public static final String COLUMN_PLACES_NAME = "place_name";

    public static final String SQL_CREATE_PLACES =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_PLACES + "("+
                    COLUMN_PLACES_ID + " INTEGER PRIMARY KEY, "+
                    COLUMN_PLACES_DISTRICTS_ID + " INTEGER, "+
                    COLUMN_PLACES_NAME + " TEXT"+ ");";
    public static final String SQL_DELETE_PLACES =
            "DROP TABLE "+TABLE_PLACES;

    /**
     * User Selected Places Table
     */
    public static final String TABLE_USER_SELECTED_PLACES = "places";
    public static final String COLUMN_USER_ID_USER_SELECTED_PLACES = "user_id";
    public static final String COLUMN_PLACES_ID_USER_SELECTED_PLACES = "place_id";

    public static final String SQL_CREATE_USER_SELECTED_PLACES =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_USER_SELECTED_PLACES + "("+
                    COLUMN_USER_ID_USER_SELECTED_PLACES + " INTEGER , "+
                    COLUMN_PLACES_ID_USER_SELECTED_PLACES + " INTEGER, "+ ");";
    public static final String SQL_DELETE_USER_SELECTED_PLACES =
            "DROP TABLE "+TABLE_USER_SELECTED_PLACES;
}
