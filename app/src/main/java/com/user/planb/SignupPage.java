package com.user.planb;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 1/20/2018.
 */

public class SignupPage extends AppCompatActivity {

    Button buttonRegister;
    EditText etName,etMail,etPhoneno,etPassword;
    TextView validateName,validateMail,validatePhoneno,validatePassword,login;
    String mailtostring,usertostring,passwordtostring,mobiletostring;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;
    String data;
    RelativeLayout relativeLayout;
    Toolbar toolbar;
    Map<Integer, String> states = new HashMap<Integer, String>();
    Map<Integer,Map<Integer,String>> districts = new HashMap<>();
    Map<Integer,Map<Integer,String>> places = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_signup);

        etName = (EditText) findViewById(R.id.etUserNameSignupPage);
        etMail = (EditText) findViewById(R.id.etEmailSignupPage);
        etPhoneno = (EditText) findViewById(R.id.etMobileNumberSignupPage);
        etPassword = (EditText) findViewById(R.id.etPasswordSignupPage);
        buttonRegister = (Button) findViewById(R.id.bSubmitSignupPage);
        toolbar = (Toolbar) findViewById(R.id.tbSignUpPage);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlMainSignupPage);
        validateName = (TextView) findViewById(R.id.tvValidUserNameSignupPage);
        validateMail = (TextView) findViewById(R.id.tvVaildEmailSignupPage);
        validatePhoneno = (TextView) findViewById(R.id.tvValidMobileNumberSignupPage);
        validatePassword = (TextView) findViewById(R.id.tvValidPasswordSignupPage);
        login = (TextView) findViewById(R.id.tvLoginSignupPage);
        progressBar = (ProgressBar) findViewById(R.id.pbSignupPage);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clSignupPage);

        progressBar.setVisibility(View.INVISIBLE);

        toolbar.setTitle("Sign Up");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.user.planb.LOGINPAGE");
                startActivity(intent);
            }
        });

        etMail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                validateMail.setText("");
                return false;
            }
        });
        etName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                validateName.setText("");
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
        etPhoneno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                validatePhoneno.setText("");
                return false;
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                mailtostring = etMail.getText().toString();
                usertostring = etName.getText().toString();
                passwordtostring = etPassword.getText().toString();
                mobiletostring = etPhoneno.getText().toString();

                boolean process = true;
                if (usertostring.equals("")) {
                    validateName.setText("Enter Valid User Name");
                    process = false;
                } else if (mailtostring.equals("")) {
                    validateMail.setText("Enter valid Email");
                    process = false;
                } else if (passwordtostring.equals("") || passwordtostring.length() < 5) {
                    validatePassword.setText("Enter valid Password. Password should be greater than 5 characters");
                    process = false;
                } else if (mobiletostring.equals("") || mobiletostring.length() < 10) {
                    validatePhoneno.setText("Enter valid Mobile Number");
                    process = false;
                } else if (validateEmail(etMail.getText().toString()) == false) {
                    validateMail.setText("Enter valid Email");
                    process = false;
                }
                if (process) {
                    boolean connection = checkConnection();
                    if(connection) {
                        try {
                            RequestPackage requestPackage = new RequestPackage();
                            requestPackage.setMethod("POST");
                            requestPackage.setUri(getResources().getString(R.string.server) + "register");
                            requestPackage.setParam("email", mailtostring);
                            requestPackage.setParam("password", passwordtostring);
                            requestPackage.setParam("user", usertostring);
                            requestPackage.setParam("mobile", mobiletostring);
                            SignupPage.SignupThread signupThread = new SignupPage.SignupThread();
                            signupThread.execute(requestPackage);
                        } catch (Exception e) {
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

    private class GenericTextWatcher implements TextWatcher {

            private View view;
            private GenericTextWatcher(View view) {
                this.view = view;
            }

            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void afterTextChanged(Editable editable) {
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

        private class SignupThread extends AsyncTask<RequestPackage,String,String> {

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
                Log.e("pradeepplanb","pradeepplanb"+content);
                JsonParser jsonParser = new JsonParser();
                if(data == null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(coordinatorLayout,"Maintainence activity is in progress. Please try after sometime",Snackbar.LENGTH_LONG).show();
                }else {
                    try {
                        dataItems = jsonParser.parseRegisterFeed(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (dataItems.getRegisterStatus().equals("successful")) {

                        DataSource source = new DataSource(SignupPage.this);
                        progressBar.setVisibility(View.INVISIBLE);
                        int userId = dataItems.getLoginUserId();
                        String userName = dataItems.getRegisterUserName();
                        SessionManagement sessionManagement = new SessionManagement(SignupPage.this);
                        sessionManagement.createLoginSession(mailtostring, passwordtostring, userId, userName, "");
                        Bundle bundle = new Bundle();
                        int user_id = dataItems.getRegisterUserId();
                        bundle.putInt("task", 0);
                        bundle.putInt("user_id", user_id);
                        Intent i = new Intent("com.user.planb.ADDPLACES");
                        i.putExtras(bundle);
                        startActivity(i);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Cannot login. " + dataItems.getRegisterErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
}
