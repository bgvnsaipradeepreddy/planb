package com.user.planb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.user.planb.MainActivity;
import com.user.planb.model.ContentData;
import com.user.planb.model.DataItems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/20/2018.
 */

public class DataSource {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DataSource(Context mContext) {
        this.mContext = mContext;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void open(){
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void close(){
        mDbHelper.close();
    }
    public void insertStates(ContentValues values){
        mDatabase.insert(SQliteTables.TABLE_STATES,null,values);
    }
    public void insertDistricts(ContentValues values){
        mDatabase.insert(SQliteTables.TABLE_DISTRICTS,null,values);
    }
    public void insertPlaces(ContentValues values){
        mDatabase.insert(SQliteTables.TABLE_PLACES,null,values);
    }

    public Map<Integer,String> getStates(){
        Map<Integer,String> states = new HashMap<>();
        String[] projection = {
                SQliteTables.COLUMN_STATE_ID,
                SQliteTables.COLUMN_STATE_NAME,
        };
        Cursor cursor = mDatabase.query(SQliteTables.TABLE_STATES,projection,null,null,null,null,null);
        while (cursor.moveToNext()){
            states.put(cursor.getInt(cursor.getColumnIndex(SQliteTables.COLUMN_STATE_ID)),cursor.getString(cursor.getColumnIndex(SQliteTables.COLUMN_STATE_NAME)));
        }
        return states;
    }
    public Map<Integer, Map<Integer,String>> getDistricts(){
        Map<Integer,String> states = new HashMap<>();
        Map<Integer, Map<Integer,String>> state_district_map = new HashMap<Integer,Map<Integer,String>>();
        String[] projection = {
                SQliteTables.COLUMN_DISTRICTS_ID,
                SQliteTables.COLUMN_DISTRICTS_NAME,
        };
        states = getStates();
        for(Integer state_key : states.keySet()){
            Map<Integer,String> districts = new HashMap<>();
            String selection = SQliteTables.COLUMN_DISTRICTS_STATE_ID + " = ?";
            String[] selectionArgs = { String.valueOf(state_key)};
            Cursor cursor = mDatabase.query(SQliteTables.TABLE_DISTRICTS,projection,selection,selectionArgs,null,null,null);
            while (cursor.moveToNext()){
                districts.put(cursor.getInt(cursor.getColumnIndex(SQliteTables.COLUMN_DISTRICTS_ID)),cursor.getString(cursor.getColumnIndex(SQliteTables.COLUMN_DISTRICTS_NAME)));
            }
            state_district_map.put(state_key,districts);
        }
        return state_district_map;
    }
    public Map<Integer, Map<Integer,String>> getPlaces(){
        Map<Integer,String> districts = new HashMap<>();
        Map<Integer, Map<Integer,String>> district_place_map = new HashMap<Integer,Map<Integer,String>>();
        Map<Integer, Map<Integer,String>> state_district_map = new HashMap<Integer,Map<Integer,String>>();
        String[] projection = {
                SQliteTables.COLUMN_PLACES_ID,
                SQliteTables.COLUMN_PLACES_NAME,
        };
        state_district_map = getDistricts();
        for(Integer state_key : state_district_map.keySet()){
            Map<Integer,String> district = new HashMap<>();
            district = state_district_map.get(state_key);
            for (Integer district_key : district.keySet()){
                Map<Integer,String> places = new HashMap<>();
                String selection = SQliteTables.COLUMN_PLACES_DISTRICTS_ID + " = ?";
                String[] selectionArgs = { String.valueOf(district_key)};
                Cursor cursor = mDatabase.query(SQliteTables.TABLE_PLACES,projection,selection,selectionArgs,null,null,null);
                while (cursor.moveToNext()){
                    places.put(cursor.getInt(cursor.getColumnIndex(SQliteTables.COLUMN_PLACES_ID)),cursor.getString(cursor.getColumnIndex(SQliteTables.COLUMN_PLACES_NAME)));
                }
                district_place_map.put(district_key,places);
            }
        }
        return district_place_map;
    }
    public void insertUserSelecedPlaces(ContentValues values){
        mDatabase.insert(SQliteTables.TABLE_USER_SELECTED_PLACES,null,values);
    }

    public Map<Integer,String> getUserSelectedPlaces(){

        String[] projection = {
                SQliteTables.COLUMN_PLACES_ID,
                SQliteTables.COLUMN_PLACES_NAME,
        };
        Map<Integer,String> key_name = new HashMap<>();
        /**
        Integer[] item = selPlaces.toArray(new Integer[selPlaces.size()]);
        Log.e("pradeepdebug10","pradeepdebug"+item);
        Arrays.sort(item);
        String[] a=Arrays.toString(item).split("[\\[\\]]")[1].split(", ");
        String[] mark = new String[a.length];
        Arrays.fill(mark, "?");
        String joined = TextUtils.join(",", mark);
        String selection = SQliteTables.COLUMN_PLACES_DISTRICTS_ID + " IN("+joined+")";
        String[] selectionArgs = (String[]) a;
        Log.e("pradeepdebug11","pradeepdebug"+a);
         */
        Cursor cursor = mDatabase.query(SQliteTables.TABLE_PLACES,projection,null,null,null,null,null);
        while (cursor.moveToNext()){
            Log.e("pradeepdebug00","00");
            key_name.put(cursor.getInt(cursor.getColumnIndex(SQliteTables.COLUMN_PLACES_ID)),cursor.getString(cursor.getColumnIndex(SQliteTables.COLUMN_PLACES_NAME)));
        }
        for(Integer xxx: key_name.keySet()){
            Log.e("pradeepdebug12","pradeepdebug"+key_name.get(xxx));
        }
        return key_name;
    }


    public String getPlaceNameFromId(int placeId){

        String[] projection = {
                SQliteTables.COLUMN_PLACES_NAME,
        };
        String place = null;
        /**
         Integer[] item = selPlaces.toArray(new Integer[selPlaces.size()]);
         Log.e("pradeepdebug10","pradeepdebug"+item);
         Arrays.sort(item);
         String[] a=Arrays.toString(item).split("[\\[\\]]")[1].split(", ");
         String[] mark = new String[a.length];
         Arrays.fill(mark, "?");
         String joined = TextUtils.join(",", mark);
         String selection = SQliteTables.COLUMN_PLACES_DISTRICTS_ID + " IN("+joined+")";
         String[] selectionArgs = (String[]) a;
         Log.e("pradeepdebug11","pradeepdebug"+a);
         */
        String selection = SQliteTables.COLUMN_PLACES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(placeId)};
        Cursor cursor = mDatabase.query(SQliteTables.TABLE_PLACES,projection,selection,selectionArgs,null,null,null);
        Log.e("cursorout","out");
        if (cursor.moveToFirst()) {
            Log.e("cursorin","in");
            place = cursor.getString(cursor.getColumnIndex(SQliteTables.COLUMN_PLACES_NAME));

        }
        return place;
    }
}
