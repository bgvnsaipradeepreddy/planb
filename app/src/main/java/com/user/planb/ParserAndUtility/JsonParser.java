package com.user.planb.ParserAndUtility;

import android.util.Log;

import com.user.planb.model.DataItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by user on 1/20/2018.
 */

public class JsonParser {
    public static DataItems parseLoginFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");

        JSONArray places  = object.optJSONArray("userSelectedPlaces");
        List<Integer> placesSel =new ArrayList<>(); //new int[places.length()];


        if(status.equals(true)){
            dataItems.setLoginStatus("successful");
            JSONArray jsonArray = object.getJSONArray("result");
            JSONObject jsonObjectChild = jsonArray.getJSONObject(0);

            int userId = jsonObjectChild.getInt("user_id");
            String userName = jsonObjectChild.getString("name");
            String userStatus = jsonObjectChild.getString("profile" +
                    "_status");
            dataItems.setLoginUserId(userId);
            dataItems.setLoginUserName(userName);
            dataItems.setLoginUserStatus(userStatus);
            for (int i = 0; i < places.length(); ++i) {
                placesSel.add(places.optInt(i));
            }
            dataItems.setUserSelectedPlaces(placesSel);
            return dataItems;
        }else {
            String errorMsg = object.getString("result");
            dataItems.setLoginStatus("unsuccessful");
            dataItems.setLoginUserId(0);
            dataItems.setLoginErrorMsg(errorMsg);
            return dataItems;
        }
    }



    public static DataItems parseRegisterFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");

        if(status.equals(true)){
            //dataItems = getPlaces(content,dataItems);
            dataItems.setRegisterStatus("successful");
            JSONArray jsonArray = object.getJSONArray("result");
            JSONObject jsonObjectChild = jsonArray.getJSONObject(0);
            int userId = jsonObjectChild.getInt("user_id");
            String userName = jsonObjectChild.getString("name");
            dataItems.setRegisterUserId(userId);
            dataItems.setRegisterUserName(userName);
            return dataItems;
        } else {
            String errorMsg = object.getString("result");
            Log.e("pradeepregister",errorMsg);
            dataItems.setRegisterStatus("unsuccessful");
            dataItems.setRegisterUserId(0);
            dataItems.setRegisterErrorMsg(errorMsg);
            return dataItems;
        }
    }

    public static DataItems parseAddSelPlacesFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");
        if(status.equals(true)){
            dataItems.setAddSelPlacesStatus("successful");
            return dataItems;
        }
        return null;
    }

    public static DataItems parseDeleteSelPlacesFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");
        if(status.equals(true)){
            dataItems.setDeleteSelPlacesStatus("successful");
            return dataItems;
        }
        return null;
    }
    public static DataItems getPlaces(String content,DataItems dataItems) throws JSONException {
        JSONObject object = null;
        Map<Integer, String> states = new HashMap<Integer, String>();
        Map<Integer, Map<Integer,String>> state_district_map = new HashMap<Integer,Map<Integer,String>>();
        Map<Integer,Map<Integer,String>> places_district_map= new HashMap<Integer,Map<Integer,String>>();
        object = new JSONObject(content);
        JSONArray jsonArray = object.getJSONArray("place_result");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
            int state_id = jsonObjectChild.getInt("state_id");
            String state = jsonObjectChild.getString("state");
            JSONArray jsonDistrictsArray = jsonObjectChild.getJSONArray("districts");
            //List<String> districts = new ArrayList<String>();
            Map<Integer, Map<Integer,String>> districts_map = new HashMap<Integer, Map<Integer,String>>();
            Log.e("array lengthe is ","array legth is "+jsonDistrictsArray.length());
            TreeMap<Integer,String> district_key_value = new TreeMap<>();
            for(int j=0;j<jsonDistrictsArray.length();j++){
                JSONObject jsonObjectdistrict = jsonDistrictsArray.getJSONObject(j);
                int district_id = jsonObjectdistrict.getInt("district_id");
                String district = jsonObjectdistrict.getString("district");
                district_key_value.put(district_id,district);
                JSONArray jsonPlacesArray = jsonObjectdistrict.getJSONArray("places");
                TreeMap<Integer,String> places= new TreeMap<Integer,String>();
                for(int k=0;k<jsonPlacesArray.length();k++) {
                    JSONObject jsonObjectplaces = jsonPlacesArray.getJSONObject(k);
                    int place_id = jsonObjectplaces.getInt("place_id");
                    String place = jsonObjectplaces.getString("place");
                    places.put(place_id,place);
                }

                places_district_map.put(district_id,places);

            }
            TreeMap<Integer,Map<Integer,String>> treeMapPlaces = new TreeMap<>(places_district_map);
            dataItems.setPlaces(treeMapPlaces);
            state_district_map.put(state_id,district_key_value);
            TreeMap<Integer,Map<Integer,String>> treeMapDistricts = new TreeMap<>(state_district_map);
            dataItems.setDistricts(treeMapDistricts);
            states.put(state_id,state);
        }
        dataItems.setDistricts(state_district_map);
        TreeMap<Integer,String> treeMapStates = new TreeMap<Integer,String>(states);
        dataItems.setStates(treeMapStates);
        return dataItems;
    }
    public static DataItems parseAddLivedPlacesFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");
        if(status.equals(true)){
            dataItems.setDeleteSelPlacesStatus("successful");
            return dataItems;
        }
        return null;
    }

    public static ArrayList<DataItems> parseLivedPlaces(String data) throws JSONException {
        JSONObject object = null;
        object = new JSONObject(data);
        JSONArray userLivedPlaces  = object.optJSONArray("userLivedPlaces");
        ArrayList<DataItems> livedPlaces = new ArrayList<>();

        for(int i = 0; i<userLivedPlaces.length();i++){
            DataItems dataItems1 = new DataItems();
            JSONObject place_ind = userLivedPlaces.getJSONObject(i);
            dataItems1.setUserLivedPlace(place_ind.getString("user_locations"));
            dataItems1.setIsItCurrentLived(place_ind.getInt("current_location"));
            dataItems1.setUserLivedStart(place_ind.getInt("start_year"));
            dataItems1.setUserLivedEnd(place_ind.getInt("end_year"));
            livedPlaces.add(dataItems1);
        }
        return livedPlaces;
    }

    public static DataItems parseQueryPostFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");

        if(status.equals(true)){
            //dataItems = getPlaces(content,dataItems);
            dataItems.setQueryStatus("successful");
            return dataItems;
        } else {
            String errorMsg = object.getString("result");
            Log.e("pradeepregister",errorMsg);
            dataItems.setRegisterStatus("unsuccessful");
            return dataItems;
        }
    }
    public static DataItems parseQueryExistsFeed(String content) throws JSONException {
        JSONObject object = null;
        DataItems dataItems = new DataItems();

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");
        ArrayList<DataItems> queries = new ArrayList<>();
        if(status.equals(true)){
            dataItems.setQueryExists("successful");
            return dataItems;
        } else {
            dataItems.setQueryExists("unsuccessful");
            return dataItems;
        }
    }
    public static ArrayList<DataItems> parseQueryFeed(String content) throws JSONException {
        JSONObject object = null;

        object = new JSONObject(content);
        ArrayList<DataItems> queries = new ArrayList<>();
            JSONArray jsonArray = object.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++) {
                Log.e("pradeepreddyg2", "pradeepreddyg" + content);
                DataItems dataItems = new DataItems();
                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
                dataItems.setQueryId(jsonObjectChild.getInt("query_id"));
                dataItems.setQueryUserName(jsonObjectChild.getString("user_name"));
                dataItems.setQueryTitle(jsonObjectChild.getString("title"));
                dataItems.setQueryContent(jsonObjectChild.getString("query_description"));
                dataItems.setQueryUserId(jsonObjectChild.getInt("user_id"));
                dataItems.setQueryEmergency(jsonObjectChild.getInt("emergency"));
                Boolean answer_status = jsonObjectChild.getBoolean("answer_status");
                dataItems.setAnswersExists(answer_status);
                if (answer_status.equals(true)) {
                    JSONArray jsonArrayAnswers = jsonObjectChild.getJSONArray("answers");
                    ArrayList<String> answers = new ArrayList<>();
                    ArrayList<Integer> answerIds = new ArrayList<>();
                    ArrayList<String> answersUsers = new ArrayList<>();
                    ArrayList<Integer> answersUserIds = new ArrayList<>();

                    for (int j = 0; j < jsonArrayAnswers.length(); j++) {
                        JSONObject jsonObjectChildAnswers = jsonArrayAnswers.getJSONObject(j);
                        answers.add(jsonObjectChildAnswers.getString("query_answer"));
                        answerIds.add(jsonObjectChildAnswers.getInt("answer_id"));
                        answersUserIds.add(jsonObjectChildAnswers.getInt("user_id"));
                        answersUsers.add(jsonObjectChildAnswers.getString("user_name"));
                    }
                    dataItems.setAnswers(answers);
                    dataItems.setAnswersUserIds(answersUserIds);
                    dataItems.setAnswersUsers(answersUsers);
                    dataItems.setAnswersUserIds(answersUserIds);
                }
                Log.e("pradeepdebug1","");
                queries.add(dataItems);
            }
            return queries;
    }
}
