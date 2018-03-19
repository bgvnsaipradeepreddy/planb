package com.user.planb;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.user.planb.database.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/22/2018.
 */

public class MainPage extends AppCompatActivity {

    Toolbar toolbar;
    SimpleCursorAdapter mAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView tabMain,tabChat,tabProfile;
    DataSource dataSource;
    Map<Integer, Map<Integer, String>> districts_places = new HashMap<>();
    List<String> places = new ArrayList<>();
    String[] final_places = new String[]{} ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);
        toolbar = (Toolbar) findViewById(R.id.tbMainPage);
        tabLayout = (TabLayout) findViewById(R.id.tlMainPage);
        viewPager = (ViewPager) findViewById(R.id.vpMainPage);
        dataSource = new DataSource(MainPage.this);


        districts_places = dataSource.getPlaces();
        for (int dis : districts_places.keySet()) {
            Map<Integer, String> place_keys = new HashMap<>();
            place_keys = districts_places.get(dis);
            for (int pla : place_keys.keySet()) {
                places.add(place_keys.get(pla));
            }
        }


        toolbar.setTitle("Plan b");
        setSupportActionBar(toolbar);

        setupViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(5);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 2){
                    tabProfile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_sentiment_satisfied_custom, 0, 0);
                }else if(tab.getPosition() == 0){
                    tabMain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_custom, 0, 0);
                }else if(tab.getPosition() == 1) {
                    tabChat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_custom, 0, 0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("saiontabunselect","saiontabunselect"+tab.getPosition());
                if(tab.getPosition() == 2){
                    Log.e("saiontabselect","saiontabselect"+tab.getPosition());
                    tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sentiment_satisfied, 0, 0);
                }else if(tab.getPosition() == 0){
                    tabMain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
                }else if(tab.getPosition() == 1) {
                    tabChat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat, 0, 0);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("saiontabreselect","saiontabreselect"+tab.getPosition());
            }
        });
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final View row = super.getView(position, convertView, parent);
                if (position % 2 == 0)
                    row.setBackgroundResource(android.R.color.white);
                else
                    row.setBackgroundResource(android.R.color.white);
                return row;
            }
        };


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentMain(), "Home");
        adapter.addFrag(new FragmentMain(), "Chat");
        adapter.addFrag(new FragmentProfile(), "Profile");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabMain = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_layout_customize, null);
        tabMain.setText("Home");
        tabMain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_custom, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabMain);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_custom);

        tabChat = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_layout_customize, null);
        tabChat.setText("Chat");
        //Drawable dd = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_chat);

        //dd.setColorFilter(new
        //        PorterDuffColorFilter(Color.parseColor("#ec1b23"),PorterDuff.Mode.MULTIPLY));
        tabChat.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabChat);
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_chat_black_customize);

        tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_layout_customize, null);
        tabProfile.setText("Profile");
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sentiment_satisfied, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabProfile);
        //tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_black_customize);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.search));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("cityName"));
                Bundle bundle = new Bundle();
                bundle.putString("place", txt);
                Intent intent = new Intent("com.hakunamatata.user.hakunamatata.USERENTITIES");
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("pradeepsai1", "pradeepsai" + s);
                populateAdapter(s);
                return true;
            }
        });

        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<places.size(); i++) {
            if (places.get(i).toLowerCase().startsWith(query.toLowerCase())) {
                Log.e("pradeepsai2","pradeepsai"+places.get(i));
                c.addRow(new Object[]{i, places.get(i)});
            }
        }
        mAdapter.changeCursor(c);
        mAdapter.notifyDataSetChanged();
    }
}

