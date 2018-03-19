package com.user.planb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.ParserAndUtility.JsonUtility;
import com.user.planb.model.DataItems;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/15/2018.
 */

public class AddUserLivedPlaces extends AppCompatActivity {

    Toolbar toolbar;
    EditText etPlace,etStartYear,etEndYear;
    TextView tvPlace;
    ProgressBar progressBar;
    CheckBox cb;
    Button tbButton;
    int checkedStatus = 0;
    String data;
    ListView listView;
    int PLACE_AUTOCOMPLETE_REQUEST = 1001;
    DataItems lvDataItems = new DataItems();
    SessionManagement sessionManagement;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_lived_user_add);
        sessionManagement = new SessionManagement(AddUserLivedPlaces.this);
        View header = View.inflate(this, R.layout.places_lived_header, null);
        toolbar = (Toolbar) findViewById(R.id.tbAddUserLivedPlaces);
        progressBar = (ProgressBar) findViewById(R.id.pbAddUserLivedPlaces);
        listView = (ListView) findViewById(R.id.lvAddUserLivedPlaces);
        etPlace = (EditText) header.findViewById(R.id.etPlacePlacesLivedHeader);
        etStartYear = (EditText) header.findViewById(R.id.etStartYearPlacesLivedHeader);
        etEndYear = (EditText) header.findViewById(R.id.etEndYearPlacesLivedHeader);
        cb = (CheckBox) header.findViewById(R.id.cbPlacesLivedHeader);
        tbButton = (Button) findViewById(R.id.bToolbarButton);

        toolbar.setTitle("select Places ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<String> selPlaces = new ArrayList<>();
        SessionManagement sessionManagement = new SessionManagement(this);
        ArrayList<DataItems> livedPlaces = new ArrayList<>();
        livedPlaces = sessionManagement.getLivedPlaces();
        if (!livedPlaces.isEmpty()) {
            for (int i = 0; i < livedPlaces.size(); i++) {
                DataItems items = new DataItems();
                items = livedPlaces.get(i);
                //lvDataItems.add(items);
                selPlaces.add(items.getUserLivedPlace());
                Log.e("fxxxxx","xx"+items.getUserLivedPlace()+"xx");
            }
        }
        PopulateLivedPlaces populateLivedPlaces = new PopulateLivedPlaces(this, selPlaces);
        listView.setAdapter(populateLivedPlaces);
        listView.addHeaderView(header);
        etPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAPlace();
            }
        });
        tbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionManagement sessionManagement = new SessionManagement(AddUserLivedPlaces.this);
                int userId = sessionManagement.getUserId();
                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri(getResources().getString(R.string.server) + "insertUserLivedPlaces");
                String output = "";
                //DataItems dataItems = new DataItems();
                lvDataItems.setUserLivedPlace(String.valueOf(etPlace.getText()));
                lvDataItems.setUserLivedStart(Integer.parseInt(etStartYear.getText().toString()));
                lvDataItems.setUserLivedEnd(Integer.parseInt(etEndYear.getText().toString()));
                if (cb.isChecked()) {
                    lvDataItems.setIsItCurrentLived(1);
                } else {
                    lvDataItems.setIsItCurrentLived(0);
                }
                //lvDataItems.add(dataItems);
                try {
                    output = JsonUtility.createLivedPlacesJson(userId, lvDataItems);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("xxxxxl","xx"+output+"xx");
                requestPackage.setParam("user_lived_places", output);
                AddLivedPlaces addLivedPlaces = new AddLivedPlaces();
                addLivedPlaces.execute(requestPackage);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }


    private void selectAPlace() {

        try {
            Intent intent;
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this,data);
                updateUI(place);
            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
            } else if(resultCode == RESULT_CANCELED){
                updateUI(null);
            }
        }
    }

    private void updateUI(Place choosenPlace) {
        if(choosenPlace == null){
            etPlace.setText("");
        }else {
            String str = choosenPlace.getName()+"\n";
            etPlace.setText(str);
        }
    }


    class PopulateLivedPlaces extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> placeInfo;

        public PopulateLivedPlaces(Activity activity, ArrayList<String> placeInfo) {
            super(activity, R.layout.places_populate, placeInfo);
            this.placeInfo = placeInfo;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.places_populate, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tvPopulatePlaces);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(7,7,7,7);
            tv.setLayoutParams(params);
            tv.setText(placeInfo.get(position));
            return convertView;
        }
    }

    private class AddLivedPlaces extends AsyncTask<RequestPackage,String,String> {

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
            Log.e("xxxxli","xx"+data+"xx");
            if(data == null){
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddUserLivedPlaces.this, "Cannot add. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {
                try {
                    dataItems = jsonParser.parseAddLivedPlacesFeed(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataItems.getDeleteSelPlacesStatus().equals("successful")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    SessionManagement sessionManagement = new SessionManagement(AddUserLivedPlaces.this);
                    ArrayList<DataItems> livedPlaces = new ArrayList<>();
                    livedPlaces = sessionManagement.getLivedPlaces();
                    livedPlaces.add(lvDataItems);
                    sessionManagement.saveLivedPlaces(livedPlaces);
                    finish();
                    startActivity(getIntent());
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddUserLivedPlaces.this, "Cannot add place. Please try after sometime", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
