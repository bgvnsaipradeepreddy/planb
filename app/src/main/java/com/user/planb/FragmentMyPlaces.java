package com.user.planb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.ParserAndUtility.JsonUtility;
import com.user.planb.database.DataSource;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2/4/2018.
 */

public class FragmentMyPlaces extends Fragment {


    ArrayList<String> selectedPlaces = new ArrayList<String>();
    int userId;
    ArrayList<String> al = new ArrayList<String>();
    ListView listView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    ImageView ivDelete;
    String data;
    ViewPager viewPage;
    ProgressBar progressBar;
    int final_output;
    List<Integer> place = new ArrayList<>();
    FragmentMyPlaces.PopulatePlaces populatePlaces;
    SessionManagement sessionManagement;


    public static FragmentMyPlaces newInstance(){

        FragmentMyPlaces fragment = new FragmentMyPlaces();
        Bundle args = new Bundle();
        return fragment;
    }


    public FragmentMyPlaces() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myplaces_fragment, container, false);
        LayoutInflater inflaterHeader = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.header_query,null);
        viewPage = (ViewPager) container;
        RelativeLayout rlHeader = (RelativeLayout) header.findViewById(R.id.rlQueryHeader);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbFragmentMyplaces);
        TextView tvHeader = (TextView) header.findViewById(R.id.tvQueryHeader);
        tvHeader.setText("Add or Remove places");
        /**
        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("already",1);
                bundle.putInt("task",0);
                Intent i = new Intent("com.user.planb.ADDPLACES");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
         */
        listView = (ListView) rootView.findViewById(R.id.lvFragmentMyPlaces);

        sessionManagement = new SessionManagement(getContext());
        place =sessionManagement.getSelPlaces();
        DataSource source = new DataSource(getContext());
        Map<Integer,String> key_name = new HashMap<>();
        key_name = source.getUserSelectedPlaces();
        final List<String> selPlaces = new ArrayList<>();
        for(int i=0;i<place.size();i++){
            selPlaces.add(key_name.get(place.get(i)));
        }

        populatePlaces = new PopulatePlaces(getActivity(), (ArrayList<String>) selPlaces);
        listView.setAdapter(populatePlaces);
        listView.addHeaderView(header);

        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("task",0);
                bundle.putInt("already",1);
                Intent i = new Intent("com.user.planb.ADDPLACES");
                i.putExtras(bundle);
                //startActivity(i);
                startActivityForResult(i, 10001);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentTransaction ft = null;
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            // recreate your fragment here
        {
            viewPage.getAdapter().notifyDataSetChanged();
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        populatePlaces.clear();
    }


    class PopulatePlaces extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> placeInfo;

        public PopulatePlaces(Activity activity, ArrayList<String> placeInfo) {
            super(activity, R.layout.places_populate, placeInfo);
            this.placeInfo = placeInfo;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.places_populate, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tvPopulatePlaces);
            ImageView ivDelete = (ImageView) convertView.findViewById(R.id.ivPopulatePlaces);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (place.size() == 1) {
                            Toast.makeText(getActivity(), "Cannot delete. Atleast one place should be present", Toast.LENGTH_LONG).show();
                        } else if (place.size() > 1) {
                            boolean connection = checkConnection();
                            if (connection) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        getActivity());
                                alertDialogBuilder
                                        .setMessage("do you want to delete?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                userId = sessionManagement.getUserId();
                                                RequestPackage requestPackage = new RequestPackage();
                                                requestPackage.setMethod("POST");
                                                requestPackage.setUri(getResources().getString(R.string.server) + "deleteSelectedPlace");
                                                String output = "";
                                                try {
                                                    output = JsonUtility.createDeleteSelPlacesJson(userId, place.get(position));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                requestPackage.setParam("user_sel_place", output);
                                                DeleteSelectedPlace deleteSelectedPlace = new DeleteSelectedPlace(position);
                                                deleteSelectedPlace.execute(requestPackage);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, just close
                                                // the dialog box and do nothing
                                                dialog.cancel();
                                            }
                                        });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
                            }else{
                                Toast.makeText(getActivity(), "No Internet connectivity", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                });
            tv.setText(placeInfo.get(position));
            return convertView;
        }
        private boolean checkConnection() {
            boolean isConnected = ConnectivityReceiver.isConnected();
            return isConnected;
        }
    }
    private class DeleteSelectedPlace extends AsyncTask<RequestPackage,String,String> {

        int position;
        public DeleteSelectedPlace(int position){
            this.position = position;
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
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Cannot delete place. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {
                Log.e("xxxx11", "xx" + data);
                try {
                    dataItems = jsonParser.parseDeleteSelPlacesFeed(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("xxxx22", "xx" + dataItems.getDeleteSelPlacesStatus());
                if (dataItems.getDeleteSelPlacesStatus().equals("successful")) {
                    final_output = 1;
                    Log.e("xxxxc", "xx" + final_output);
                    progressBar.setVisibility(View.INVISIBLE);
                    place.remove(position);
                    SessionManagement sessionManagement = new SessionManagement(getActivity());
                    sessionManagement.saveSelPlaces(place);
                    viewPage.getAdapter().notifyDataSetChanged();
                } else {
                    final_output = 0;
                    Log.e("xxxxc", "xx" + final_output);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Cannot delete place. Please try after sometime", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
