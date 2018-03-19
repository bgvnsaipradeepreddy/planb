package com.user.planb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by user on 2/12/2018.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    LinearLayout llPlaces;
    public BottomSheetFragment() {
            // Required empty public constructor
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.snack_user_profile, container, false);
            llPlaces = (LinearLayout) view.findViewById(R.id.llplacesProfileUserSnack);
            llPlaces.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent("com.user.planb.ADDUSERLIVEDPLACES");
                    startActivity(intent);
                }
            });
            return view;
        }
}
