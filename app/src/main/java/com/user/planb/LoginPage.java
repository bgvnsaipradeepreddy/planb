package com.user.planb;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 1/19/2018.
 */

public class LoginPage extends AppCompatActivity {

    Toolbar toolbar;
    EditText etMail,etPassword;
    String mailtostring,passwordtostring;
    TextView validateMail,validatePassword,signup;
    Button bLogin;
    String data;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);

        toolbar = (Toolbar) findViewById(R.id.tbLoginPage);
        bLogin = (Button) findViewById(R.id.bSubmitLoginPage);
        signup = (TextView) findViewById(R.id.tvSingupLoginPage);
        etMail = (EditText) findViewById(R.id.etEmailLoginPage);
        etPassword = (EditText) findViewById(R.id.etPasswordLoginPage);
        validateMail = (TextView) findViewById(R.id.tvValidEmailLogin);
        validatePassword = (TextView) findViewById(R.id.tvValidPasswordLogin);
        progressBar = (ProgressBar) findViewById(R.id.pbLoginPage);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clLoginPage);

        progressBar.setVisibility(View.INVISIBLE);

        toolbar.setTitle("Log In");
        //set listener for signup click
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.user.planb.SIGNUPPAGE");
                startActivity(intent);
            }
        });

        //on touch listener for mail and password edit
        etMail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                validateMail.setText("");
                return false;
            }
        });
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                validatePassword.setText("");
                return false;
            }
        });

        //on click listener for submission
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                mailtostring = etMail.getText().toString();
                passwordtostring = etPassword.getText().toString();
                boolean process = true;
                if(mailtostring.equals("")){
                    validateMail.setText("Enter valid Email");
                    process = false;
                }
                else if(passwordtostring.equals("")){
                    validatePassword.setText("Enter valid Password.");
                    process = false;
                }
                else if(validateEmail(etMail.getText().toString()) == false){
                    validateMail.setText("Enter valid Email");
                    process = false;
                }
                if(process) {
                    boolean connection = checkConnection();
                    if(connection){
                        try {
                            RequestPackage requestPackage = new RequestPackage();
                            requestPackage.setMethod("POST");
                            requestPackage.setUri(getResources().getString(R.string.server)+"login");
                            requestPackage.setParam("email",mailtostring);
                            requestPackage.setParam("password",passwordtostring);
                            LoginThread loginThread = new LoginThread();
                            loginThread.execute(requestPackage);

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Snackbar.make(coordinatorLayout,"No Internet connect. Please connect and try again",Snackbar.LENGTH_LONG).show();
                    }

                }
            }

        });
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }


    private class LoginThread extends AsyncTask<RequestPackage,String,String> {

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
            ArrayList<DataItems> lived_places = new ArrayList<>();
            JsonParser jsonParser = new JsonParser();
            if(data == null){
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordinatorLayout,"Maintainence activity is in progress. Please try after sometime",Snackbar.LENGTH_LONG).show();
            }else {
                try {
                    dataItems = jsonParser.parseLoginFeed(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataItems.getLoginStatus().equals("successful")) {
                    try {
                        lived_places = jsonParser.parseLivedPlaces(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("xxxxxz","xx"+data);
                    progressBar.setVisibility(View.INVISIBLE);
                    int userId = dataItems.getLoginUserId();
                    String userName = dataItems.getLoginUserName();
                    String userStatus = dataItems.getLoginUserStatus();
                    List<Integer> userSelectedPlaces = new ArrayList<>();
                    userSelectedPlaces = dataItems.getUserSelectedPlaces();
                    List<Integer> userEmeSel = new ArrayList<>();
                    userEmeSel = dataItems.getUserEmeSelected();
                    SessionManagement sessionManagement = new SessionManagement(LoginPage.this);
                    sessionManagement.createLoginSession(mailtostring, passwordtostring, userId, userName, userStatus);
                    sessionManagement.saveSelPlaces(userSelectedPlaces);
                    sessionManagement.saveLivedPlaces(lived_places);
                    sessionManagement.saveUserEmeQueries(userEmeSel);
                    if (userSelectedPlaces.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("user_id", userId);
                        Intent intent = new Intent("com.user.planb.MAINPAGE");
                        startActivity(intent);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("user_id", userId);
                        Intent intent = new Intent("com.user.planb.ADDPLACES");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginPage.this, "Cannot login. " + dataItems.getLoginErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean validateEmail(final String emailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }
}
