package com.user.planb;

/**
 * Created by user on 1/26/2018.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.database.DataSource;
import com.user.planb.model.ContentData;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;
import com.user.planb.DataSeralize;

import org.json.JSONException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.user.planb.R.id.textView;

public class FragmentQueryAlt extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener,Serializable {

    ProgressBar progressBar;
    String http_output;
    String http_output1;
    ListView listView;
    LinearLayout llInternet;
    LinearLayout llEmergency;
    PopulateQueries populateQueries;
    TextView tvInternet;
    View view;
    View header;
    int place_id,userId;
    Button bInternet;
    FloatingActionButton fabquery;
    ArrayList<DataItems> dataItemses = new ArrayList<>();
    ArrayList<Integer> emerSel = new ArrayList<>();

    public static FragmentQueryAlt newInstance(int id) {

        FragmentQueryAlt fragment = new FragmentQueryAlt();
        Bundle args = new Bundle();
        args.putInt("place_id", id);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentQueryAlt() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("saireddyres","saireddyres");
        ConnectivityStarter.getInstance().setConnectivityListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.query_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.lvFragmentQuery);
        llInternet = (LinearLayout) view.findViewById(R.id.llInternetFragmentQuery);
        tvInternet = (TextView) view.findViewById(R.id.tvInternetFragmentQuery);
        bInternet = (Button) view.findViewById(R.id.bInternetFragmentQuery);
        progressBar = (ProgressBar) view.findViewById(R.id.pbFragmentQuery);
        progressBar.setVisibility(View.VISIBLE);
        place_id = getArguments().getInt("place_id");
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        userId = sessionManagement.getUserId();
        header = inflater.inflate(R.layout.header_query,null);
        RelativeLayout rlHeader = (RelativeLayout) header.findViewById(R.id.rlQueryHeader);
        TextView tvHeader = (TextView) header.findViewById(R.id.tvQueryHeader);

        tvHeader.setText("Post Query");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tbPlacesEntities);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        emerSel = sessionManagement.getUserEmeQueries();
        //fabquery = (FloatingActionButton) view.findViewById(R.id.fabFragmetQuery);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("place_id",place_id);
                Intent intent = new Intent("com.user.planb.POSTQUERY");
                //Intent intent = new Intent("com.hakunamatata.hakunamatata.EXPANDEXAMPLE");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if(checkConnection()) {

            String placeId = Integer.toString(place_id);
            Log.e("saireddy2","saireddy"+placeId);
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("POST");
            requestPackage.setUri(getResources().getString(R.string.server) + "getQueries");
            requestPackage.setParam("placeId", placeId);
            GetQueries getQueries = new GetQueries();
            getQueries.execute(requestPackage);

        }else {
            Log.e("saireddy3","saireddy");
            listView.setVisibility(View.INVISIBLE);
            llInternet.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void setViewStatus(View vg2, int status) {

        vg2.setVisibility(status);
    }

    public AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //setViewStatus(toolbar, View.GONE);

                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //scroll was stopped, let's show search bar again
                        //setViewStatus(searchView, View.VISIBLE);
                        setViewStatus(fabquery, View.VISIBLE);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        //user is scrolling, let's hide search bar
                        //setViewStatus(searchView, View.GONE);
                        setViewStatus(fabquery, View.GONE);
                        break;
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        };


    }
    private boolean checkConnection() {
        Log.e("saireddy4","saireddy");
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        modifyConnectivity(isConnected);
    }

    private void modifyConnectivity(boolean isConnected) {
        Log.e("saireddy6","saireddy");
        if(isConnected) {
            Log.e("saireddy7","saireddy");
            listView.setVisibility(View.VISIBLE);
            llInternet.setVisibility(View.INVISIBLE);
            //populateQueries = new PopulateQueries(getActivity(), dataItemses);
            //listView.setAdapter(populateQueries);
            listView.addHeaderView(header);
        }else {
            Log.e("saireddy8","saireddy");
            listView.setVisibility(View.INVISIBLE);
            llInternet.setVisibility(View.VISIBLE);
        }
    }

    class PopulateQueries extends ArrayAdapter {
        LayoutInflater li;
        Context context;
        ArrayList<DataItems> queries;

        public PopulateQueries(Activity activity,ArrayList<DataItems> queries) {

            super(activity, R.layout.queries_populate_alt,queries);
            li = activity.getWindow().getLayoutInflater();
            this.queries = queries;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ArrayList<String> answers = new ArrayList<>();
            ArrayList<String> answersUserName = new ArrayList<>();
            ArrayList<Integer> answersUserIds = new ArrayList<>();
            ArrayList<Integer> answersIds = new ArrayList<>();
            final DataItems dataItems = queries.get(position);
            Log.e("position","position is "+position+"query is "+dataItems.getQueryTitle());
            convertView = li.inflate(R.layout.queries_populate_alt, parent, false);
            final String userName,userInfo,title,description;
            final int queryEmergency,queryId;
            title = dataItems.getQueryTitle();
            TextView emergency = (TextView) convertView.findViewById(R.id.tvEmergencyPopulateQueriesAlt);

            TextView query = (TextView) convertView.findViewById(R.id.tvQueryPopulateQueriesAlt);
            query.setText(title);
            description = dataItems.getQueryContent();
            llEmergency = (LinearLayout) convertView.findViewById(R.id.llEmergencyPopulateQueriesAlt);

            final StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
            userName = "asked by "+dataItems.getQueryUserName();
            SpannableStringBuilder sb = new SpannableStringBuilder(userName);
            int start = userName.indexOf(dataItems.getQueryUserName());
            int end = start + dataItems.getQueryUserName().length();
            sb.setSpan(boldStyle, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            queryId = dataItems.getQueryId();
            queryEmergency = dataItems.getQueryEmergency();
            if(queryEmergency > 5){
                llEmergency.setVisibility(View.GONE);
                emergency.setVisibility(View.VISIBLE);
            } else {
                if(emerSel.contains(queryId)){
                    llEmergency.setVisibility(View.GONE);
                }else{
                    llEmergency.setVisibility(View.VISIBLE);
                }
            }


            Button bYesEmergency = (Button) convertView.findViewById(R.id.bYesEmergencyPopulateQueriesAlt);
            bYesEmergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    emerSel.add(queryId);
                    if(checkConnection()) {
                        String query = Integer.toString(queryId);
                        String user = Integer.toString(userId);
                        RequestPackage requestPackage = new RequestPackage();
                        requestPackage.setMethod("POST");
                        requestPackage.setUri(getResources().getString(R.string.server) + "addUserSelectedEmer");
                        requestPackage.setParam("query_id", query);
                        requestPackage.setParam("user_id", user);
                        AddEmergency addEmergency = new AddEmergency();
                        addEmergency.execute(requestPackage);

                    }else {
                        Log.e("saireddy3","saireddy");
                        listView.setVisibility(View.INVISIBLE);
                        llInternet.setVisibility(View.VISIBLE);
                    }

                }
            });


            //Log.e("krishna","krishna"+finalAnswersIds1.size());//+"size "+finalAnswersIds1.size());

            TextView askUser = (TextView) convertView.findViewById(R.id.tvAskUserPopulateQueriesAlt);
            askUser.setText(sb);

            userInfo = dataItems.getQueryUserInfo();

            Boolean answerExist = dataItems.getAnswersExists();
            if(answerExist.equals(true)){
                RelativeLayout rlAnswers = (RelativeLayout) convertView.findViewById(R.id.rlAnswersPopulateQueriesAlt);
                rlAnswers.setVisibility(View.VISIBLE);

                answers = dataItems.getAnswers();
                answersUserName = dataItems.getAnswersUsers();
                answersUserIds = dataItems.getAnswersUserIds();
                answersIds = dataItems.getAnswerIds();
                ExpandableTextView expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.etvDescriptionPopulateQueriesAlt);
                expandableTextView.setText(answers.size()+" answers available");

            }else {
                RelativeLayout rlAnswers = (RelativeLayout) convertView.findViewById(R.id.rlAnswersPopulateQueriesAlt);
                rlAnswers.setVisibility(View.VISIBLE);
                ExpandableTextView expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.etvDescriptionPopulateQueriesAlt);
                expandableTextView.setText("No answers");
            }
            TextView date = (TextView) convertView.findViewById(R.id.tvDatePopulateQueriesAlt);
            Date datePost = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date.setText("Posted on "+sdf.format(datePost));
            //queryDetails = dataItems.getQueryContent();


            final ArrayList<String> finalAnswers = answers;
            final ArrayList<String> finalAnswersUserName = answersUserName;
            final ArrayList<Integer> finalAnswersUserIds = answersUserIds;
            final ArrayList<Integer> finalAnswersIds = answersIds;
            query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userName",userName);
                    bundle.putString("title",title);
                    bundle.putInt("queryId",queryId);
                    bundle.putString("description",description);
                    bundle.putStringArrayList("answers", finalAnswers);
                    bundle.putStringArrayList("answersUserName", finalAnswersUserName);
                    bundle.putIntegerArrayList("answerUserIds", finalAnswersUserIds);
                    bundle.putIntegerArrayList("answerIds", finalAnswersIds);
                    bundle.putInt("query_emergency",queryEmergency);
                    Intent intent = new Intent("com.user.planb.QUERYANSWERS");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
    private class GetQueries extends AsyncTask<RequestPackage,String,String> {

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

            JsonParser jsonParser = new JsonParser();
            if(http_output == null){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Maintainence activity is in progress. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {

                try {
                    dataItems = jsonParser.parseQueryExistsFeed(http_output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pradeepreddyg","pradeepreddyg"+dataItems);
                if (dataItems.getQueryExists().equals("successful")) {
                    try {
                        Log.e("dataitmeses1","data"+dataItemses);
                        dataItemses = jsonParser.parseQueryFeed(http_output,userId);
                        Log.e("dataitmeses2","data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //DataItems da = dataItemses.get(1);
                    //Log.e("position00","0"+da.getQueryTitle());
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e("insideelse","isideelse"+dataItemses.size());

                    if(dataItemses.size()>0) {

                        Log.e("insideif","if");
                        populateQueries = new PopulateQueries(getActivity(), dataItemses);
                        listView.setAdapter(populateQueries);
                        listView.addHeaderView(header);

                    }else {
                        Log.e("insideelse","isideelse");
                        llInternet.setVisibility(View.VISIBLE);
                        tvInternet.setText("There are no queries posted by other guys. If you have added any queries check those in you profile");
                        bInternet.setVisibility(View.INVISIBLE);
                        populateQueries = new PopulateQueries(getActivity(), dataItemses);
                        listView.setAdapter(populateQueries);
                        listView.addHeaderView(header);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    //listView.setVisibility(View.INVISIBLE);
                    llInternet.setVisibility(View.VISIBLE);
                    tvInternet.setText("There are no queries posted by other guys. If you have added any queries check those in you profile");
                    bInternet.setVisibility(View.INVISIBLE);
                    populateQueries = new PopulateQueries(getActivity(), dataItemses);
                    listView.setAdapter(populateQueries);
                    listView.addHeaderView(header);
                }
            }
        }
    }

    private class AddEmergency extends AsyncTask<RequestPackage,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            http_output1 = httpManager.getData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            DataItems dataItems = new DataItems();

            JsonParser jsonParser = new JsonParser();
            if (http_output1 == null) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Maintainence activity is in progress. Please try after sometime", Toast.LENGTH_LONG).show();
            } else {

                try {
                    dataItems = jsonParser.parseAddLivedPlacesFeed(http_output1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pradeepreddyg", "pradeepreddyg" + dataItems);
                if (dataItems.getDeleteSelPlacesStatus().equals("successful")) {
                    llEmergency.setVisibility(View.GONE);
                    progressBar.setVisibility(View.INVISIBLE);
                    SessionManagement sessionManagement = new SessionManagement(getActivity());
                    sessionManagement.saveUserEmeQueries(emerSel);
                    Toast.makeText(getActivity(), "You have made the query as Emergency.", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Cannot add this as emergency. Please try after sometime", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}