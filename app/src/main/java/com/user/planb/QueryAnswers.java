package com.user.planb;

import android.app.Activity;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 3/13/2018.
 */

public class QueryAnswers extends AppCompatActivity {

    PopulateQueryAnswers populateQueryAnswers;
    ListView listView;
    Toolbar toolbar;
    String userName,description,title;
    ArrayList<String> answers = new ArrayList<>();
    ArrayList<String> answersUsers = new ArrayList<>();
    ArrayList<Integer> answersUserIds = new ArrayList<>();
    ArrayList<Integer> answersIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_query);
        Bundle bundle = getIntent().getExtras();
        userName=bundle.getString("userName");
        title = bundle.getString("title");
        description = bundle.getString("description");
        answers = bundle.getStringArrayList("answers");
        answersUsers = bundle.getStringArrayList("answersUserName");

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.answers_query_header,null);
        TextView askedUser = (TextView) header.findViewById(R.id.tvUserHeaderQueryAnswers);
        TextView answerCount = (TextView) header.findViewById(R.id.tvAnswerCountHeaderQueryAnswers);
        ExpandableTextView etv = header.findViewById(R.id.etvHeaderQueryAnswers);
        TextView tvTitle = header.findViewById(R.id.tvHeaderQueryAnswers);
        askedUser.setText(userName);
        tvTitle.setText(title);
        if(description != null && !description.isEmpty()){
            etv.setVisibility(View.VISIBLE);
            etv.setText(description);
        }

        int count = answers.size();
        answerCount.setText(count+" answers");
        toolbar = (Toolbar) findViewById(R.id.tbIncludeQueryAnswers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.lvQueryAnswers);
        populateQueryAnswers = new PopulateQueryAnswers(QueryAnswers.this,answers,answersUsers,answersIds,answersUserIds,count);
        listView.addHeaderView(header);
        listView.setAdapter(populateQueryAnswers);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
    class PopulateQueryAnswers extends ArrayAdapter {
        LayoutInflater li;
        Context context;

        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> answersUsers = new ArrayList<>();
        ArrayList<Integer> answersUserIds = new ArrayList<>();
        ArrayList<Integer> answersIds = new ArrayList<>();
        int count;

        public PopulateQueryAnswers(Activity activity,ArrayList<String> answers,ArrayList<String> answersUsers,ArrayList<Integer> answersUserIds,ArrayList<Integer> answersIds,int count){

            super(activity, R.layout.answers_query_populate);
            Log.e("inside new","inside new");
            li = activity.getWindow().getLayoutInflater();
            this.answers = answers;
            this.answersUsers = answersUsers;
            this.count = count;
        }

        @Override
        public int getCount() {
            return answers.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.answers_query_populate, parent, false);

            if(count > 0){

                TextView textView = (TextView) convertView.findViewById(R.id.tvDescriptionPopulateQueryAnswers);

                ImageView img1 = (ImageView) convertView.findViewById(R.id.ivPopulateQueryAnswers);
                Bitmap decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.ic_person);


                Bitmap resized = Bitmap.createScaledBitmap(decodedByte,80,80, true);
                Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
                img1.setImageBitmap(conv_bm);
                TextView name = (TextView) convertView.findViewById(R.id.tvNamePopulateQueryAnswers);
                textView.setText(answers.get(position));
                Date datePosted = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                name.setText(answersUsers.get(position));
                Log.e("end of get view","end of get view");
            }
            return convertView;
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

}

