package com.user.planb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2/5/2018.
 */

public class FragmentUserProfile extends Fragment {
    View view;
    ImageView ivUserImage;
    Button bLogout;
    TextView userName,userStatus,editHighlightProfile;


    public static FragmentUserProfile newInstance() {

        FragmentUserProfile fragment = new FragmentUserProfile();
        Bundle args = new Bundle();
        return fragment;
    }

    public FragmentUserProfile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.profile_user_fragment, container, false);

        ivUserImage = (ImageView) view.findViewById(R.id.ivFragmentUserProfile);
        bLogout = (Button) view.findViewById(R.id.bLogoutFragmentUserProfile);
        userName = (TextView) view.findViewById(R.id.tvNameFragmentUserProfile);
        userStatus = (TextView) view.findViewById(R.id.tvStatusFragmentUserProfile);
        editHighlightProfile = (TextView) view.findViewById(R.id.tvEditHighFragmentUserProfile);
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        Map<String,String> userInfo = new HashMap<>();
        userInfo = sessionManagement.getUserInfo();

        Log.e("xxxx","status is **"+userInfo.get("profile_status")+"**");
        if (userInfo.get("user_name") != null || !userInfo.get("user_name").isEmpty()) {
            userName.setText(userInfo.get("user_name"));
        }else{
            userName.setText("Anonymous");
        }
        userStatus.setText("No Status.Please Update");
        if((String)userInfo.get("profile_status") == null ||  ((String)userInfo.get("profile_status")).equals("null")){
            userStatus.setText("No Status.Please Update");
            userStatus.setText(userInfo.get("profile_status"));
            Log.e("saipradeep","value is "+userInfo.get("profile_status"));
        }else{
            userStatus.setText(userInfo.get("profile_status"));
            Log.e("saipradeep","value is "+userInfo.get("profile_status"));
        }

        editHighlightProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        Bitmap decodedByte;
        decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.ic_person);



        Bitmap resized = Bitmap.createScaledBitmap(decodedByte,80,80, true);
        Bitmap conv_bm = getRoundedCornerBitmap(resized, 120);
        ivUserImage.setImageBitmap(conv_bm);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(getActivity());
                sessionManagement.logoutUser();
            }
        });

        return view;
    }

    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //int borderWidth=2;
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        //Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        radius = radius-10;
        Log.e("radius","radius"+radius);
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        //paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - 2 / 2, paint);
        return output;
    }

}
