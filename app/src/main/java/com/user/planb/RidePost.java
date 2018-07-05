package com.user.planb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.database.DataSource;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 4/14/2018.
 */

public class RidePost extends AppCompatActivity {

    EditText etStartPlace;
    EditText etStartAddress;
    EditText etDestPlace;
    EditText etDestAddress;
    static EditText etStartDate;
    EditText etStartTime;
    EditText etCost;
    Toolbar toolbar;
    String http_output;
    ProgressBar progressBar;
    RadioButton rbCar,rbMotorcycle;
    Spinner spinner;
    Button tbButton;
    int START_ADDRESS = 1000;
    int DEST_PLACE = 1001;
    int DEST_ADDRESS = 1002;
    String[] items = new String[]{
            "1","2","3","4"
    };
    int placeId;
    int poolSelect = 0;
    String startAddress,destPlace,destAddress,startDate,startTime,startPlaceId,spinnerValue,cost,poolType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_ride);
        Bundle bundle = getIntent().getExtras();
        final String startPlace = bundle.getString("place");
        placeId = bundle.getInt("place_id");
        etStartPlace = (EditText) findViewById(R.id.etStartRidePost);
        etStartAddress = (EditText) findViewById(R.id.etStartAddressRidePost);
        etDestPlace = (EditText) findViewById(R.id.etDesRidePost);
        etDestAddress = (EditText) findViewById(R.id.etDesAddressRidePost);
        etStartDate = (EditText) findViewById(R.id.etDateRidePost);
        etStartTime = (EditText) findViewById(R.id.etTimeRidePost);
        rbCar = (RadioButton) findViewById(R.id.rbCarRidePost);
        rbMotorcycle = (RadioButton) findViewById(R.id.rbMotorcycleRidePost);
        spinner = (Spinner) findViewById(R.id.spinnerRidePost);
        etCost = (EditText) findViewById(R.id.etCostRidePost);
        toolbar = (Toolbar) findViewById(R.id.tbIncludeRidePost);
        tbButton = (Button) findViewById(R.id.bToolbarButton);
        progressBar = (ProgressBar) findViewById(R.id.pbRidePost);

        toolbar.setTitle("Post Ride");

        etStartPlace.setText(startPlace);
        etStartPlace.setEnabled(false);
        etDestPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAPlace(DEST_PLACE);
            }
        });

        etStartAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAPlace(START_ADDRESS);
            }
        });
        etDestAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAPlace(DEST_ADDRESS);
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RidePost.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etStartTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RidePost.this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rbCar.setOnCheckedChangeListener(new Radio_check());
        rbMotorcycle.setOnCheckedChangeListener(new Radio_check());

        tbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //InputMethodManager inputManager = (InputMethodManager)
                //       getSystemService(Context.INPUT_METHOD_SERVICE);

                //inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                //        InputMethodManager.HIDE_NOT_ALWAYS);
                startAddress = etStartAddress.getText().toString();
                destPlace = etDestPlace.getText().toString();
                destAddress = etDestAddress.getText().toString();
                startDate = etStartDate.getText().toString();
                startTime = etStartTime.getText().toString();
                startPlaceId = Integer.toString(placeId);
                cost = etCost.getText().toString();
                boolean process = true;
                if (destPlace.equals("")) {
                    Toast.makeText(RidePost.this, "Please select the destination place to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                else if (startDate.equals("")) {
                    Toast.makeText(RidePost.this, "Please select the date of journey to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                else if (startTime.equals("")) {
                    Toast.makeText(RidePost.this, "Please select the start time to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                else if (cost.equals("")) {
                    Toast.makeText(RidePost.this, "Please select the destination place to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                else if (spinnerValue.equals("")) {
                    Toast.makeText(RidePost.this, "Please select number of seats available to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                else if(poolSelect == 0){
                    Toast.makeText(RidePost.this, "Please select car or bike to proceed", Toast.LENGTH_LONG).show();
                    process = false;
                }
                SessionManagement sessionManagement = new SessionManagement(RidePost.this);
                int user_id = sessionManagement.getUserId();
                String userId = Integer.toString(user_id);
                poolType = Integer.toString(poolSelect);

                if (process) {
                    if (checkConnection()) {
                        try {
                            RequestPackage requestPackage = new RequestPackage();
                            requestPackage.setMethod("POST");
                            requestPackage.setUri(getResources().getString(R.string.server) + "insertPool");
                            requestPackage.setParam("userId", userId);
                            requestPackage.setParam("placeId",startPlaceId);
                            requestPackage.setParam("costSeat",cost);
                            requestPackage.setParam("dstAddress",destAddress);
                            requestPackage.setParam("dstPlace",destPlace);
                            requestPackage.setParam("seatsAvailable",spinnerValue);
                            requestPackage.setParam("startAddress",startAddress);
                            requestPackage.setParam("startDate",startDate);
                            requestPackage.setParam("startTime",startTime);
                            requestPackage.setParam("poolType",poolType);
                            RidePost.AddPool addPool = new RidePost.AddPool();
                            addPool.execute(requestPackage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(RidePost.this, "No Internet connect. Please connect and try again", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis()+(1000*60*60*24*31));
            return  dialog;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            etStartDate.setText(year+"-"+month+"-"+day);
        }
    }
    private void selectAPlace(int result) {

        try {
            Intent intent;
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent,result);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DEST_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this,data);
                updateDestPlace(place);
            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
            } else if(resultCode == RESULT_CANCELED){
                updateDestPlace(null);
            }
        }
        if(requestCode == START_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this,data);
                updateStartAddress(place);
            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
            } else if(resultCode == RESULT_CANCELED){
                updateStartAddress(null);
            }
        }
        if(requestCode == DEST_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this,data);
                updateDestAddress(place);
            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
            } else if(resultCode == RESULT_CANCELED){
                updateDestAddress(null);
            }
        }
    }
    private void updateDestPlace(Place choosenPlace) {
        if(choosenPlace == null){
            etDestPlace.setText("");
        }else {
            String str = choosenPlace.getName()+"\n";
            etDestPlace.setText(str);
        }
    }

    private void updateDestAddress(Place choosenPlace) {
        if(choosenPlace == null){
            etDestAddress.setText("");
        }else {
            String str = choosenPlace.getAddress().toString()+"\n";
            etDestAddress.setText(str);
        }
    }

    private void updateStartAddress(Place choosenPlace) {
        if(choosenPlace == null){
            etStartAddress.setText("");
        }else {
            String str = choosenPlace.getAddress().toString()+"\n";
            etStartAddress.setText(str);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
    private class Radio_check implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(rbCar.isChecked()){
                poolSelect = 2;
                spinner.setEnabled(true);
                spinner.setClickable(true);
            }
            else if(rbMotorcycle.isChecked()){
                poolSelect = 1;
                spinner.setSelection(0);
                spinner.setEnabled(false);
                spinner.setClickable(false);
            }
        }
    }

    private class AddPool extends AsyncTask<RequestPackage,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            http_output = httpManager.getData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            DataItems dataItems = new DataItems();
            Log.e("pradeepplanb", "pradeepplanb" + content);
            JsonParser jsonParser = new JsonParser();
            if (http_output == null) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RidePost.this, "Maintainence activity is in progress. Please try after sometime", Toast.LENGTH_LONG).show();
            } else {
                try {
                    dataItems = jsonParser.parsePoolPostFeed(http_output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataItems.getPoolStatus().equals("successful")) {

                    DataSource source = new DataSource(RidePost.this);
                    progressBar.setVisibility(View.INVISIBLE);
                    setResult(Activity.RESULT_OK);
                    finish();
                    Toast.makeText(RidePost.this, "Your query has been succesfully posted. ", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RidePost.this, "Cannot Post. Try after sometime. "
                            , Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
