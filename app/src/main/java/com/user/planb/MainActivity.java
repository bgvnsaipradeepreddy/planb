package com.user.planb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.database.DBHelper;
import com.user.planb.database.DataSource;
import com.user.planb.model.ContentData;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.user.planb.SessionManagement.KEY_ID;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    String data;
    RelativeLayout rlInternet,rlMain;
    Button bTryAgain;
    Toolbar toolbar;
    TextView tvConnectivity;

    Map<String,Integer> totalCount = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlInternet = (RelativeLayout) findViewById(R.id.rlInternetMainActivity);
        rlMain = (RelativeLayout) findViewById(R.id.rlMainActivity);
        bTryAgain = (Button) findViewById(R.id.bMainActivity);
        tvConnectivity = (TextView) findViewById(R.id.tvMainActivity);
        toolbar = (Toolbar) findViewById(R.id.tbMainActivity);
        toolbar.setTitle("Plan b");
        toolbar.setVisibility(View.INVISIBLE);
        rlMain.setBackgroundResource(R.drawable.planb);
        final SessionManagement sessionManagement = new SessionManagement(MainActivity.this);

        try {
            if(sessionManagement.checkLogin()){
                List<Integer> userSelPlaces = sessionManagement.getSelPlaces();
                if(!userSelPlaces.isEmpty()) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent("com.user.planb.MAINPAGE");
                    startActivity(intent);
                }else{
                    int userId = sessionManagement.getUserId();
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",userId);
                    Intent intent = new Intent("com.user.planb.ADDPLACES");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainSub();
                    }
                }, 3000);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainSub();
            }
        });

    }

    private void mainSub() {
        boolean connection = checkConnection();
        SessionManagement sm = new SessionManagement(MainActivity.this);
        if(connection){
            rlInternet.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.GONE);
            rlMain.setBackgroundResource(R.drawable.planb);
            try {
                if(sm.checkPlacesIn()) {
                    createTable();
                }
                else{

                    Intent i = new Intent("com.user.planb.LOGINPAGE");
                    // Closing all the Activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Staring Login Activity
                    startActivity(i);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            rlMain.setBackgroundResource(R.color.textColorPrimry);
            rlInternet.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle("Plan b");
        }
    }


    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }


    private void createTable() {
        SQLiteOpenHelper sqLiteOpenHelper = new DBHelper(MainActivity.this);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri(getResources().getString(R.string.server) + "getStates");
        StatesThread statesThread = new StatesThread();
        statesThread.execute(requestPackage);
    }

    protected void onPause(){
        super.onPause();
    }


    private class StatesThread extends AsyncTask<RequestPackage,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if(data == null ){
                rlMain.setBackgroundResource(R.color.textColorPrimry);
                rlInternet.setVisibility(View.VISIBLE);
                tvConnectivity.setText("Some error has occured. Please try again after some time");
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle("Plan b");
            }else {
                try {
                    dataItems = jsonParser.getPlaces(data, dataItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataSource source = new DataSource(MainActivity.this);
                ContentData contentData = new ContentData();
                totalCount = contentData.insertPlaces(dataItems, source);
                SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                sessionManagement.placesIn(totalCount);
                Intent i = new Intent("com.user.planb.LOGINPAGE");
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                startActivity(i);
            }
        }
    }
}
