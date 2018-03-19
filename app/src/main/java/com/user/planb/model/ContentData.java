package com.user.planb.model;

import android.content.ContentValues;

import com.user.planb.database.DataSource;
import com.user.planb.database.SQliteTables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/20/2018.
 */

public class ContentData {

    public static Map insertPlaces(DataItems dataItems,DataSource source){
        Map<Integer, String> states = new HashMap<Integer, String>();
        Map<Integer,Map<Integer,String>> districts = new HashMap<>();
        Map<Integer,Map<Integer,String>> places = new HashMap<>();

        states = dataItems.getStates();
        districts = dataItems.getDistricts();
        places = dataItems.getPlaces();

        Map<String,Integer> total_count = new HashMap<>();
        total_count.put("states",states.size());
        total_count.put("districts",districts.size());
        total_count.put("places",places.size());

        for(Integer state_key : states.keySet()){
            ContentValues values = statesToValues(state_key,states.get(state_key));
            source.insertStates(values);
            Map<Integer,String> districts_states = new HashMap<>();
            districts_states = districts.get(state_key);
            for(Integer district_key : districts_states.keySet()){
                ContentValues valuesDistricts = districtsToValues(district_key,districts_states.get(district_key),state_key);
                source.insertDistricts(valuesDistricts);
                Map<Integer,String> places_districts = new HashMap<>();
                places_districts = places.get(district_key);
                for (Integer place_key : places_districts.keySet()){
                    ContentValues valuesPlaces = placesToValues(place_key,places_districts.get(place_key),district_key);
                    source.insertPlaces(valuesPlaces);
                }
            }
        }
        return total_count;
    }
    public static ContentValues statesToValues(Integer id,String stateName){
        ContentValues values = new ContentValues();
        values.put(SQliteTables.COLUMN_STATE_ID,id);
        values.put(SQliteTables.COLUMN_STATE_NAME,stateName);
        return values;
    }

    public static ContentValues districtsToValues(Integer id,String districtName,int stateId){
        ContentValues values = new ContentValues();
        values.put(SQliteTables.COLUMN_DISTRICTS_ID,id);
        values.put(SQliteTables.COLUMN_DISTRICTS_NAME,districtName);
        values.put(SQliteTables.COLUMN_DISTRICTS_STATE_ID,stateId);
        return values;
    }

    public static ContentValues placesToValues(Integer id,String placeName,int districtId){
        ContentValues values = new ContentValues();
        values.put(SQliteTables.COLUMN_PLACES_ID,id);
        values.put(SQliteTables.COLUMN_PLACES_NAME,placeName);
        values.put(SQliteTables.COLUMN_PLACES_DISTRICTS_ID,districtId);
        return values;
    }

    public static void insertUserSelecedPlaces(int userId,List<Integer> places,DataSource source){
        for(int i=0;i<places.size();i++){
            ContentValues values = userSelectedPlacesToValues(userId,places.get(i));
            source.insertUserSelecedPlaces(values);
        }
    }

    private static ContentValues userSelectedPlacesToValues(int userId, Integer integer) {
        ContentValues values = new ContentValues();
        values.put(SQliteTables.COLUMN_USER_ID_USER_SELECTED_PLACES,userId);
        values.put(SQliteTables.COLUMN_PLACES_ID_USER_SELECTED_PLACES,integer);
        return values;
    }
}
