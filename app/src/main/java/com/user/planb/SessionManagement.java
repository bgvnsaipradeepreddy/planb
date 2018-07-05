
package com.user.planb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.user.planb.model.DataItems;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/19/2018.
 */

public class SessionManagement {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PlanBPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "userId";
    public static final String KEY_NAME = "userName";
    public static final String KEY_USER_STATUS = "userStatus";

    public static final String PLACES_INSERTED = "IsPlacesIn";

    public static final String PLACES_SELECTED = "placesSelected";

    public static final String PLACES_COUNT = "placesCount";

    public static final String DISTRICTS_COUNT = "districtsCount";

    public static final String STATES_COUNT = "statesCount";

    public static final String PLACES_LIVED = "placesLived";

    public static final String QUERIES_USER_SELECTED = "userEmeSelected";
    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create a login session once user logged in
    public void createLoginSession(String email, String password,int userId,String userName,String userStatus){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putInt(KEY_ID,userId);

        editor.putString(KEY_NAME,userName);
        editor.putString(KEY_USER_STATUS,userStatus);

        // commit changes
        editor.commit();
    }
    public Map<String,String> getUserInfo(){
        Map<String,String> info = new HashMap<>();
        String user = pref.getString(KEY_NAME,"");
        String status = pref.getString(KEY_USER_STATUS,"");
        info.put("user_name",user);
        info.put("profile_status",status);
        return info;
    }
    //check if the user already logged in. If logged go to home page(UserHomePage) else go to login and register page(MainPage)
    public boolean checkLogin() throws SQLException {
        // Check login status
        if(!this.isLoggedIn()){
            return false;
        }
        else {
            return true;
        }
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginPage.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Get Login State
    public boolean isLoggedIn()
    {
        Log.e("pradeep reddy","pradee");
        return pref.getBoolean(IS_LOGIN, false);
    }

    //Create a login session once user logged in
    public void placesIn(Map<String,Integer> total_count){
        editor.putBoolean(PLACES_INSERTED, true);

        editor.putInt(STATES_COUNT,total_count.get("states"));
        editor.putInt(DISTRICTS_COUNT,total_count.get("districts"));
        editor.putInt(PLACES_COUNT,total_count.get("places"));
        // commit changes
        editor.commit();
    }
    public boolean checkPlacesIn() throws SQLException {
        // Check login status
        if(!this.isPlacesIn()){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isPlacesIn()
    {
        Log.e("pradeep reddy","pradee");
        return pref.getBoolean(PLACES_INSERTED, false);
    }
    public void saveSelPlaces(List<Integer> places){

        Gson gson = new Gson();
        String json = gson.toJson(places);
        editor.putString(PLACES_SELECTED, json);
        editor.commit();     // This line is IMPORTANT !!!
    }
    public ArrayList<Integer> getSelPlaces(){
        Gson gson = new Gson();
        List<Integer> places = new ArrayList<>();
        String j = gson.toJson(places);
        String json = pref.getString(PLACES_SELECTED, j);
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveLivedPlaces(ArrayList<DataItems> places){
        Gson gson = new Gson();
        String json = gson.toJson(places);
        Log.e("xx","xx"+json+"xx");
        editor.putString(PLACES_LIVED, json);
        editor.commit();     // This line is IMPORTANT !!!
    }
    public ArrayList<DataItems> getLivedPlaces(){
        Gson gson = new Gson();
        String json = pref.getString(PLACES_LIVED, null);
        Log.e("xxxxx","xx"+json+"xx");
        ArrayList<DataItems> places = new ArrayList<>();
        if(json !=null){
            Type type = new TypeToken<ArrayList<DataItems>>(){}.getType();
            places=gson.fromJson(json, type);
            return places;
        }
        return places;
    }

    public int getUserId(){
        int userId = pref.getInt(KEY_ID,0);
        return userId;
    }

    public void saveUserEmeQueries(List<Integer> queries){

        Gson gson = new Gson();
        String json = gson.toJson(queries);
        editor.putString(QUERIES_USER_SELECTED, json);
        editor.commit();     // This line is IMPORTANT !!!
    }
    public ArrayList<Integer> getUserEmeQueries(){
        Gson gson = new Gson();
        List<Integer> places = new ArrayList<>();
        String j = gson.toJson(places);
        String json = pref.getString(QUERIES_USER_SELECTED, j);
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
