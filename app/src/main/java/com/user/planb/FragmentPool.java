package com.user.planb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentPool extends Fragment {

    View view;
    ListView listPool;
    FragmentPool.PopulatePool populatePool;
    LinearLayout llInternet;
    ProgressBar progressBar;
    TextView tvInternet;
    Button bInternet;
    View header;
    int place_id,userId;
    String place;
    String http_output;
    ArrayList<DataItems> dataItemses = new ArrayList<>();

    public static FragmentPool newInstance(int id,String place) {

        FragmentPool fragment = new FragmentPool();
        Bundle args = new Bundle();
        args.putInt("place_id", id);
        args.putString("place",place);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPool() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pool_fragment, container, false);

        header = inflater.inflate(R.layout.header_query,null);
        RelativeLayout rlHeader = (RelativeLayout) header.findViewById(R.id.rlQueryHeader);
        TextView tvHeader = (TextView) header.findViewById(R.id.tvQueryHeader);
        tvHeader.setText("Post Ride");
        place_id = getArguments().getInt("place_id");
        place = getArguments().getString("place");
        listPool =(ListView) view.findViewById(R.id.lvFragmentPool);
        llInternet = (LinearLayout) view.findViewById(R.id.llInternetFragmentPool);
        bInternet = (Button) view.findViewById(R.id.bInternetFragmentPool);
        tvInternet = (TextView) view.findViewById(R.id.tvInternetFragmentPool);
        progressBar = (ProgressBar) view.findViewById(R.id.pbFragmentPool);
        progressBar.setVisibility(View.INVISIBLE);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("place_id",place_id);
                bundle.putString("place",place);
                Intent intent = new Intent("com.user.planb.RIDEPOST");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        userId = sessionManagement.getUserId();
        if(checkConnection()) {
            String placeId = Integer.toString(place_id);
            Log.e("saireddy2","saireddy"+placeId);
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("POST");
            requestPackage.setUri(getResources().getString(R.string.server) + "getPool");
            requestPackage.setParam("placeId", placeId);
            FragmentPool.GetPool getPool = new FragmentPool.GetPool();
            getPool.execute(requestPackage);
        }else {
            Log.e("saireddy3","saireddy");
            listPool.setVisibility(View.INVISIBLE);
            llInternet.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private boolean checkConnection() {
        Log.e("saireddy4","saireddy");
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    class PopulatePool extends ArrayAdapter {
        LayoutInflater li;
        //ArrayList<String> events = new ArrayList<>();
        int userId;
        ImageButton bookmark;
        TextView tvStartTime, tvEndTime, tvStartPlace, tvStartSub, tvEndPlace, tvEndSub, tvcost, tvSeats, tvUserName, tvPoolPhoneVerification, tvPoolEmailVerification;
        ImageView ivVehicle;
        LinearLayout linearLayout;
        TextView time, venue, synopsis, title;
        String poolStartTime, poolEndTime, poolStartPlace, poolEndPlace, poolStartAddress, poolEndAddress, poolDescription, poolUserName, poolCar, poolLuguage;
        int poolType, poolCost, poolSeats, poolPhoneVerification, poolEmailVerification;

        public PopulatePool(Activity activity) {
            super(activity, R.layout.pool_populate);
        }


        ArrayList<DataItems> pool;

        public PopulatePool(Activity activity,ArrayList<DataItems> pool) {

            super(activity, R.layout.queries_populate,pool);
            li = activity.getWindow().getLayoutInflater();
            this.pool = pool;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.pool_populate, parent, false);
            final DataItems poolItems = pool.get(position);
            //TextView startTime = (TextView) convertView.findViewById(R.id.tvStartTimePopulatePool);
            LinearLayout poolResult = (LinearLayout) convertView.findViewById(R.id.llPopulatePool);
            TextView startDate = (TextView) convertView.findViewById(R.id.tvstartDatePopulatePool);
            TextView startPlace = (TextView) convertView.findViewById(R.id.tvStartPlacePopulatePool);
            TextView startAddress = (TextView) convertView.findViewById(R.id.tvStartPlace1PopulatePool);
            ImageView poolType = (ImageView) convertView.findViewById(R.id.ivPoolVehiclePopulatePool);
            //TextView dstTime = (TextView) convertView.findViewById(R.id.tvEndTimePopulatePool);
            TextView dstPlace = (TextView) convertView.findViewById(R.id.tvEndPlacePopulatePool);
            TextView dstAddress = (TextView) convertView.findViewById(R.id.tvEndPlace1PopulatePool);
            TextView seats = (TextView) convertView.findViewById(R.id.tvSeatsAvailPopulatePool);
            TextView cost = (TextView) convertView.findViewById(R.id.tvCostPopulatePool);
            TextView userName = (TextView) convertView.findViewById(R.id.tvNamePopulatePool);

            startDate.setText("Journey Data:" + poolItems.getPoolStartDate());
            startPlace.setText(place);
            startAddress.setText(poolItems.getPoolStartAddress());
            if(poolItems.getPoolType() == 1){
                poolType.setImageResource(R.drawable.ic_motorcycle_black_24dp);
            }else {
                poolType.setImageResource(R.drawable.ic_directions_car_black_24dp);
            }
            dstPlace.setText(poolItems.getPoolDstPlace());
            dstAddress.setText(poolItems.getPoolDstAddress());
            seats.setText(poolItems.getPoolSeats()+" seats available");
            cost.setText(poolItems.getPoolCost()+ " per seat");
            userName.setText(poolItems.getPoolUserName());
            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivPopulatePool);
            Bitmap decodedByte;
            if(position == 0) {
                decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.ic_person);
            }else {
                decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.ic_person);
            }

            Bitmap resized = Bitmap.createScaledBitmap(decodedByte,80,80, true);
            Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
            img1.setImageBitmap(conv_bm);


            poolResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pool_start_time", poolItems.getPoolStartTime());
                    bundle.putString("pool_start_place", place);
                    bundle.putString("pool_start_address", poolItems.getPoolStartAddress());
                    bundle.putString("pool_end_place", poolItems.getPoolDstPlace());
                    bundle.putString("pool_end_address", poolItems.getPoolDstAddress());
                    bundle.putInt("pool_cost", poolItems.getPoolCost());
                    bundle.putString("pool_user_name", poolItems.getPoolUserName());
                    bundle.putString("pool_description", poolItems.getPoolUserComments());
                    bundle.putInt("pool_type", poolItems.getPoolType());
                    bundle.putInt("pool_seats", poolItems.getPoolSeats());
                    //bundle.putString("pool_luguage",poolLuguage);
                    bundle.putInt("pool_email_verification",poolItems.getPoolEmailVerification());
                    bundle.putInt("pool_phone_verification",poolItems.getPoolPhoneVerification());

                    Intent intent = new Intent("com.user.planb.POOLRESULT");
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            return convertView;
        }
    }
        public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int borderWidth = 2;
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            //Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            radius = radius - 10;
            Log.e("radius", "radius" + radius);
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
            return output;
        }

    private class GetPool extends AsyncTask<RequestPackage,String,String> {

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
                    dataItems = jsonParser.parsePoolExistsFeed(http_output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("pradeepreddyg","pradeepreddyg"+dataItems);
                if (dataItems.getPoolExists().equals("successful")) {
                    try {
                        Log.e("dataitmeses1","data"+dataItemses);
                        dataItemses = jsonParser.parsePoolFeed(http_output,userId);
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
                        populatePool = new FragmentPool.PopulatePool(getActivity(),dataItemses);
                        listPool.setAdapter(populatePool);
                        listPool.addHeaderView(header);
                    }else {
                        Log.e("insideelse","isideelse");
                        llInternet.setVisibility(View.VISIBLE);
                        tvInternet.setText("There are no queries posted by other guys. If you have added any queries check those in you profile");
                        bInternet.setVisibility(View.INVISIBLE);
                        populatePool = new FragmentPool.PopulatePool(getActivity(), dataItemses);
                        listPool.setAdapter(populatePool);
                        listPool.addHeaderView(header);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    //listView.setVisibility(View.INVISIBLE);
                    llInternet.setVisibility(View.VISIBLE);
                    tvInternet.setText("There are no queries posted by other guys. If you have added any queries check those in you profile");
                    bInternet.setVisibility(View.INVISIBLE);
                    populatePool = new FragmentPool.PopulatePool(getActivity(), dataItemses);
                    listPool.setAdapter(populatePool);
                    listPool.addHeaderView(header);
                }
            }
        }
    }

}
