package com.user.planb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.ParserAndUtility.JsonUtility;
import com.user.planb.database.DataSource;
import com.user.planb.model.ContentData;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 1/20/2018.
 */

public class AddPlaces extends AppCompatActivity {


    ExpandableListView expListView;
    List<Integer> listDataHeader = new ArrayList<Integer>();
    HashMap<Integer, List<Integer>> listDataChild = new HashMap<Integer, List<Integer>>();
    List<Integer> tempStore = new ArrayList<Integer>();
    List<Integer> seletedItems    = new ArrayList<>();
    List<Integer> userSelPlaces = new ArrayList<>();
    boolean[] checkedStatus;

    List<Integer> header;
    List<Integer> child;
    Toolbar toolbar;
    Button button;
    AlertDialog dialog;
    CoordinatorLayout coordinatorLayout;
    String data;
    int exist;
    int userId;
    ProgressBar progressBar;
    Map<Integer, String> states = new HashMap<Integer, String>();
    Map<Integer, Map<Integer, String>> districts = new HashMap<>();
    Map<Integer, Map<Integer, String>> places = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_add);
        Bundle bundle = getIntent().getExtras();
        exist = bundle.getInt("already");
        userId = bundle.getInt("user_id");
        DataSource source = new DataSource(AddPlaces.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbAddPlaces);
        progressBar = (ProgressBar) findViewById(R.id.pbAddPlaces);
        progressBar.setVisibility(View.INVISIBLE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clAddPlaces);
        expListView = (ExpandableListView) findViewById(R.id.elvAddPlaces);
        toolbar = (Toolbar) findViewById(R.id.tbAddPlaces);
        button = (Button) findViewById(R.id.bToolbarButton);
        button.setText("Add");

        toolbar.setTitle("Add Places");
        Log.e("xxxxxexist","xxx"+exist);
        if(exist == 1){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seletedItems.size() > 0){
                    boolean connection = checkConnection();
                    if(connection){

                        SessionManagement sessionManagement = new SessionManagement(AddPlaces.this);
                        userSelPlaces =  sessionManagement.getSelPlaces();
                        userSelPlaces.addAll(seletedItems);
                        Log.e("xxxxx1","xxx"+userSelPlaces);
                        Log.e("xxxxx","xxx"+seletedItems);
                        progressBar.setVisibility(View.VISIBLE);
                        //sessionManagement.saveSelPlaces( userSelPlaces);
                        //userId = sessionManagement.getUserId();
                        RequestPackage requestPackage = new RequestPackage();
                        requestPackage.setMethod("POST");
                        requestPackage.setUri(getResources().getString(R.string.server)+"addUserSelectedPlaces");
                        String output = "";
                        try {
                            output = JsonUtility.createAddSelPlacesJson(userId,seletedItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestPackage.setParam("user_sel_places",output);
                        Log.e("xxxx4","xx"+exist);
                        AddPlaces.AddSelectedPlaces addSelectedPlaces = new AddSelectedPlaces(exist);
                        addSelectedPlaces.execute(requestPackage);

                    }else {
                        Snackbar.make(coordinatorLayout,"No Internet connect. Please connect and try again",Snackbar.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                    "Select atlease one place to proceed",
                    Toast.LENGTH_SHORT).show();
                }

            }
        });
        states = source.getStates();
        districts = source.getDistricts();
        places = source.getPlaces();
        prepareListData();
        ExpandableList listAdapter = new ExpandableList(AddPlaces.this, listDataHeader, states, listDataChild, districts);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, final int childPosition, long id) {
                JsonParser jsonParser = new JsonParser();
                //Integer get_dei_is = listDataChild.get(listDataHeader.get(groupPosition))
                //        .get(childPosition);
                Integer state_id = listDataHeader.get(groupPosition);
                List<Integer> district = listDataChild.get(state_id);
                Integer district_id = district.get(childPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaces.this);

                SessionManagement sessionManagement = new SessionManagement(AddPlaces.this);
                //List<Integer> place = new ArrayList<>();
                userSelPlaces =  sessionManagement.getSelPlaces();

                Map<Integer, String> places_dis = places.get(district_id);
                final ArrayList<Integer> place_id = new ArrayList<Integer>();
                for (Integer place_id_key : places_dis.keySet()) {
                    place_id.add(place_id_key);
                }

                if(!userSelPlaces.isEmpty()){
                    place_id.removeAll(userSelPlaces);
                }

                final String[] placesel = new String[place_id.size()];
                //Arrays.fill( placesel, null );
                for (int i = 0; i < place_id.size(); i++) {
                    placesel[i] = places_dis.get(place_id.get(i));

                }
                builder.setTitle(Html.fromHtml("<font color='#c24946'>Select Places</font>"));
                checkedStatus = new boolean[place_id.size()];



                Iterator<Integer> iterator = null;
                for (int i = 0; i < placesel.length; i++) {
                    checkedStatus[i] = false;
                    if(!userSelPlaces.isEmpty()) {
                        iterator = userSelPlaces.iterator();
                        while (iterator.hasNext()) {
                            Integer id_scope = iterator.next();
                            if (id_scope == place_id.get(i)) {
                                checkedStatus[i] = true;
                                break;
                            }
                        }
                    }
                    if(!seletedItems.isEmpty()) {
                        iterator = seletedItems.iterator();
                        while (iterator.hasNext()) {
                            Integer id_scope = iterator.next();
                            if (id_scope == place_id.get(i)) {
                                checkedStatus[i] = true;
                                break;
                            }
                        }
                    }
                }

                builder.setMultiChoiceItems(placesel, checkedStatus,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            // indexSelected contains the index of item (of which checkbox checked)

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected,
                                                boolean isChecked) {

                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    // write your code when user checked the checkbox
                                    //tempStore.add(placesel[indexSelected]);
                                    tempStore.add(place_id.get(indexSelected));
                                } else if (seletedItems.contains(place_id.get(indexSelected))) {
                                    // Else, if the item is already in the array, remove it
                                    // write your code when user Uchecked the checkbox
                                    tempStore.remove(place_id.get(indexSelected));
                                    seletedItems.remove(place_id.get(indexSelected));
                                    //tempStore.remove(placesel[indexSelected]);
                                }
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                for (int i = 0; i < tempStore.size(); i++) {
                                    seletedItems.add(tempStore.get(i));
                                }
                                tempStore.clear();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel

                            }
                        });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }


    private void prepareListData() {
        header = new ArrayList<>();
        // Adding child data
        Log.e("pradeep", "pradeep" + states.keySet().size());
        for (Integer key : states.keySet()) {
            listDataHeader.add(key);
            Map<Integer, String> dist_id = new HashMap<>();
            dist_id = districts.get(key);
            List<Integer> dist_state = new ArrayList<>();
            for (Integer key_district : dist_id.keySet()) {
                dist_state.add(key_district);
            }
            listDataChild.put(key, dist_state);
        }
    }


    private class AddSelectedPlaces extends AsyncTask<RequestPackage,String,String> {

        int exist;
        public AddSelectedPlaces(int exist){
            this.exist = exist;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            data = httpManager.getData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            DataItems dataItems = new DataItems();
            JsonParser jsonParser = new JsonParser();
            if(data == null){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddPlaces.this, "Cannot add places. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {
                Log.e("xxxxx3", "xxx" + exist);
                try {
                    dataItems = jsonParser.parseAddSelPlacesFeed(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataItems.getAddSelPlacesStatus().equals("successful")) {
                    SessionManagement sessionManagement = new SessionManagement(AddPlaces.this);
                    sessionManagement.saveSelPlaces(userSelPlaces);
                    progressBar.setVisibility(View.INVISIBLE);
                    if(exist == 1){
                        setResult(Activity.RESULT_OK);
                        finish();
                    }else {
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent("com.user.planb.MAINPAGE");
                        startActivity(intent);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddPlaces.this, "Cannot add places. Please try after sometime", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}