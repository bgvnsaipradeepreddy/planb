package com.user.planb;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by user on 2/4/2018.
 */


public class FragmentProfile extends Fragment {

    android.os.Handler handle = new Handler();
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    int userId;

    public static FragmentProfile newInstance(){

        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        return fragment;
    }

    public FragmentProfile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.profile_fragment, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.vpFragmentProfile);
//        setupViewPager(viewPager);


        tabLayout = (TabLayout) view.findViewById(R.id.tlFragmentProfile);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("My places"));
        tabLayout.addTab(tabLayout.newTab().setText("My Queries"));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(),R.color.black_customize));
        //tabLayout.setSelectedTabIndicatorHeight(0);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        userId = sessionManagement.getUserId();
        FragmentProfile.PagerAdapter adapter = new FragmentProfile.PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount(),userId);
        viewPager.setAdapter(adapter);
        //viewPager.getAdapter().notifyDataSetChanged();
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
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tvTabLayout);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        viewPager.setCurrentItem(0,false);
        return view;
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        int userId;

        public PagerAdapter(FragmentManager fm, int NumOfTabs,int userId) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.userId = userId;
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return FragmentUserProfile.newInstance();
                case 1:
                    return FragmentMyPlaces.newInstance();
                case 2:
                    return FragmentUserQueries.newInstance(userId);

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
