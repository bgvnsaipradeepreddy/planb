package com.user.planb;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by user on 1/26/2018.
 */

public class PlacesEntities extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ProgressBar progressBar;
    String selectedPlace;
    int place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entities_places);
        final Bundle bundle = getIntent().getExtras();
        selectedPlace = bundle.getString("place");
        place_id = bundle.getInt("place_id");
        Log.e("pradeepreddy",selectedPlace);
        toolbar = (Toolbar) findViewById(R.id.tbPlacesEntities);
        toolbar.setTitle(selectedPlace);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tlPlacesEntities);
        viewPager = (ViewPager) findViewById(R.id.vpPlacesEntities);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.addTab(tabLayout.newTab().setText("Ask"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Deals"));
        tabLayout.addTab(tabLayout.newTab().setText("Pooling"));
        tabLayout.addTab(tabLayout.newTab().setText("Place Info"));
        tabLayout.addTab(tabLayout.newTab().setText("ChatRoom"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tvTabLayout);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }


        viewPager.setCurrentItem(0,false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
//                    return FragmentQueries.newInstance(userId,selectedPlace);
                    return FragmentQuery.newInstance(place_id);
                case 1:
//                    return FragmentEvents.newInstance(userId,selectedPlace);
                    return FragmentQuery.newInstance(place_id);
                case 2:
                    //return FragmentClimate.newInstance(userId,selectedPlace);
                    return FragmentQuery.newInstance(place_id);
                case 3:
                    return FragmentQuery.newInstance(place_id);
                case 4:
                    return FragmentQuery.newInstance(place_id);
                case 5:
                    //return FragmentEvent.newInstance(userId,selectedPlace);
                    return FragmentQuery.newInstance(place_id);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}


