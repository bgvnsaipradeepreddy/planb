package com.user.planb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by user on 5/5/2018.
 */

public class PoolResult extends AppCompatActivity {

    Toolbar toolbar;
    ImageView img1,ivPoolVehiclePoolResult,ivPoolPhoneVerification,ivPoolEmailVerification;
    CheckBox checkbox;
    TextView tandc;
    String poolStartTime,poolEndTime,poolStartPlace,poolEndPlace,poolStartAddress,poolEndAddress,poolDescription,poolUserName,poolCar,poolLuguage;
    TextView tvPoolStartTime,tvPoolEndTime,tvPoolStartPlace,tvPoolEndPlace,tvPoolStartAddress,tvPoolEndAddress,tvPoolDescription,tvPoolUserName,tvPoolCar,tvPoolLuguage,tvPoolType,tvPoolCost,tvPoolSeats,tvPoolPhoneVerification,tvPoolEmailVerification;
    int poolType,poolCost,poolSeats,poolPhoneVerification,poolEmailVerification;
    private int[] poolVerImage = {R.drawable.ic_check_circle_custom,R.drawable.ic_cancel_custom};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_pool);
        Bundle bundle = getIntent().getExtras();
        //poolStartTime = bundle.getString("pool_start_time");
        poolStartPlace = bundle.getString("pool_start_place");
        poolStartAddress = bundle.getString("pool_start_address");
        //poolEndTime = bundle.getString("pool_end_time");
        poolEndPlace = bundle.getString("pool_end_place");
        poolEndAddress = bundle.getString("pool_end_address");
        poolCost = bundle.getInt("pool_cost");
        poolCar = bundle.getString("pool_car");
        poolUserName = bundle.getString("pool_user_name");
        poolDescription = bundle.getString("pool_description");
        poolType = bundle.getInt("pool_type");
        poolSeats = bundle.getInt("pool_seats");
        //poolLuguage = bundle.getString("pool_luguage");
        poolPhoneVerification = bundle.getInt("pool_phone_verification");
        poolEmailVerification = bundle.getInt("pool_email_verification");

        img1 = (ImageView) findViewById(R.id.ivPoolResult);
        ivPoolVehiclePoolResult = (ImageView)findViewById(R.id.ivPoolVehiclePoolResult);
        checkbox = (CheckBox)findViewById(R.id.cbTCPoolResult);
        tandc = (TextView)findViewById(R.id.tvTCPoolResult);
        tvPoolStartPlace = (TextView) findViewById(R.id.tvStartPlacePoolResult);
        tvPoolStartAddress = (TextView) findViewById(R.id.tvStartPlace1PoolResult);
        tvPoolEndPlace = (TextView) findViewById(R.id.tvEndPlacePoolResult);
        tvPoolEndAddress = (TextView) findViewById(R.id.tvEndPlace1PoolResult);
        tvPoolDescription = (TextView) findViewById(R.id.tvDescPoolResult);
        tvPoolUserName = (TextView) findViewById(R.id.tvNamePoolResult);
        tvPoolCar = (TextView) findViewById(R.id.tvCarPoolResult);
        tvPoolLuguage = (TextView) findViewById(R.id.tvLugguagePoolResult);
        tvPoolCost = (TextView) findViewById(R.id.tvCostPoolResult);
        tvPoolSeats = (TextView) findViewById(R.id.tvSeatsAvailPoolResult);
        tvPoolEmailVerification = (TextView) findViewById(R.id.tvEmailVerPoolResult);
        tvPoolPhoneVerification = (TextView) findViewById(R.id.tvPhNoVerPoolResult);
        ivPoolEmailVerification = (ImageView) findViewById(R.id.ivEmailPoolResult);
        ivPoolPhoneVerification = (ImageView) findViewById(R.id.ivPhNoPoolResult);

        if(poolType == 1){
            ivPoolVehiclePoolResult.setImageResource(R.drawable.ic_motorcycle_black_24dp);
        }else {
            ivPoolVehiclePoolResult.setImageResource(R.drawable.ic_directions_car_black_24dp);
        }
        //tvPoolStartTime.setText(poolStartTime);
        tvPoolStartPlace.setText(poolStartPlace);
        tvPoolStartAddress.setText(poolStartAddress);
        //tvPoolEndTime.setText(poolEndTime);
        tvPoolEndPlace.setText(poolEndPlace);
        tvPoolEndAddress.setText(poolEndAddress);
        tvPoolDescription.setText(poolDescription);
        tvPoolUserName.setText(poolUserName);
        //tvPoolCar.setText(poolCar);
        //tvPoolLuguage.setText(poolLuguage);
        tvPoolCost.setText(Html.fromHtml(poolCost+"/person"));
        SpannableStringBuilder spanCost = new SpannableStringBuilder();
        spanCost.append("₹ " +Integer.toString(poolCost));
        int end = spanCost.length();
        spanCost.append("/person");
        spanCost.setSpan(new ForegroundColorSpan(0xFFEC1B23), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanCost.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPoolCost.setText(spanCost);
        SpannableStringBuilder spanSeats = new SpannableStringBuilder();
        spanSeats.append(Integer.toString(poolSeats));
        int endSeat = spanSeats.length();
        spanSeats.append(" seats available");
        spanSeats.setSpan(new ForegroundColorSpan(0xFFEC1B23), 0, endSeat, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spanSeats.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, endSeat, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPoolSeats.setText(spanSeats);
        Log.e("emailverpradeep","email"+poolPhoneVerification+" "+poolEmailVerification);
        if(poolPhoneVerification == 1){
            tvPoolPhoneVerification.setText("Verified");
            ivPoolPhoneVerification.setImageResource(poolVerImage[0]);
        }else{
            tvPoolPhoneVerification.setText("Not Verified");
            ivPoolPhoneVerification.setImageResource(poolVerImage[1]);
        }
        if(poolEmailVerification == 1){
            tvPoolEmailVerification.setText("Verified");
            ivPoolEmailVerification.setImageResource(poolVerImage[0]);
        }else{
            tvPoolEmailVerification.setText("Not Verified");
            ivPoolEmailVerification.setImageResource(poolVerImage[1]);
        }

        checkbox.setText("");
        tandc.setText(Html.fromHtml("I have read and agree to the " +
                "<a href='com.hakunamatata.user.hakunamatata.TermsAndConditions://Kode'>TERMS AND CONDITIONS</a>"));
        tandc.setClickable(true);
        tandc.setMovementMethod(LinkMovementMethod.getInstance());
        toolbar = (Toolbar) findViewById(R.id.tbPoolResult);
        toolbar.setTitle("Pooling");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerPoolResult);
        String[] items = new String[poolSeats];
        for(int i=1; i<=poolSeats;i++){
            items[i-1] = i+ " seat";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        Bitmap decodedByte = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_person);

        Bitmap resized = Bitmap.createScaledBitmap(decodedByte,80,80, true);
        Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
        img1.setImageBitmap(conv_bm);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int borderWidth=2;
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
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return output;
    }

}
