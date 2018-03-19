package com.user.planb.ParserAndUtility;

import com.user.planb.model.DataItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 1/25/2018.
 */

public class JsonUtility {
    public static String createAddSelPlacesJson(int userId,List<Integer> selPlaces) throws JSONException {
        JSONObject object = new JSONObject();
        JSONArray places = new JSONArray(selPlaces);
        object.put("user_id",userId);
        object.put("sel_places",places);
        return object.toString();
    }
    public static String createDeleteSelPlacesJson(int userId,int selPlaces) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("user_id",userId);
        object.put("sel_places",selPlaces);
        return object.toString();
    }
    public static String createLivedPlacesJson(int userId,DataItems dataItems) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("user_id",userId);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObjectChild = new JSONObject();
        jsonObjectChild.put("user_location",dataItems.getUserLivedPlace());
        jsonObjectChild.put("current_location",dataItems.getIsItCurrentLived());
        jsonObjectChild.put("start_year",dataItems.getUserLivedStart());
        jsonObjectChild.put("end_year",dataItems.getUserLivedEnd());
        jsonArray.put(jsonObjectChild);
        object.put("lived_places",jsonArray);
        return object.toString();
    }
}
