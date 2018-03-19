package com.user.planb;

/**
 * Created by user on 1/26/2018.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.user.planb.database.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMain extends Fragment {

    ArrayList<String> selPlaces = new ArrayList<String>();
    ListView listView;
    PopulateSelectePlaces populateSelectePlaces;


    public static FragmentMain newInstance(){

        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        return fragment;
    }


    public FragmentMain() {
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
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvFragmentMain);

        SessionManagement sessionManagement = new SessionManagement(getContext());
        final List<Integer> place = sessionManagement.getSelPlaces();
        DataSource source = new DataSource(getContext());
        Map<Integer,String> key_name = new HashMap<>();
        key_name = source.getUserSelectedPlaces();
        final List<String> selPlaces = new ArrayList<>();
        final ArrayList<Integer> selPlacesKeys = new ArrayList<>(key_name.keySet());
        for(int i=0;i<place.size();i++){
            selPlaces.add(key_name.get(place.get(i)));
        }

        populateSelectePlaces = new PopulateSelectePlaces(getActivity(), (ArrayList<String>) selPlaces);
        listView.setAdapter(populateSelectePlaces);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("place", selPlaces.get(position));
                bundle.putInt("place_id",place.get(position));
                Intent intent = new Intent("com.user.planb.PLACESENTITIES");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        populateSelectePlaces.clear();
    }

    class PopulateSelectePlaces extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> placeInfo;

        public PopulateSelectePlaces(Activity activity, ArrayList<String> placeInfo) {
            super(activity, R.layout.places_selected_populate, placeInfo);
            this.placeInfo = placeInfo;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.places_selected_populate, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tvPopulateSelectedPlaces);
            tv.setText(placeInfo.get(position));
            return convertView;
        }
    }
}

