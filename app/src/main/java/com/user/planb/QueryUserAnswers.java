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
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 3/13/2018.
 */

public class QueryUserAnswers extends AppCompatActivity {

    PopulateQueryAnswers populateQueryAnswers;
    ListView listView;
    Toolbar toolbar;
    String userName,description,title;
    Button bAnswer;
    String data,place,date;
    Integer queryId;
    ArrayList<String> answers = new ArrayList<>();
    ArrayList<String> answersUsers = new ArrayList<>();
    ArrayList<Integer> answersUserIds = new ArrayList<>();
    ArrayList<Integer> answersIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_query);
        Bundle bundle = getIntent().getExtras();
        //userName=bundle.getString("userName");
        title = bundle.getString("title");
        queryId = bundle.getInt("queryId");
        description = bundle.getString("description");
        answers = bundle.getStringArrayList("answers");
        answersUsers = bundle.getStringArrayList("answersUserName");
        place = bundle.getString("placeName");


        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.answers_query_header,null);
        TextView askedUser = (TextView) header.findViewById(R.id.tvUserHeaderQueryAnswers);
        TextView answerCount = (TextView) header.findViewById(R.id.tvAnswerCountHeaderQueryAnswers);
        ExpandableTextView etv = header.findViewById(R.id.etvHeaderQueryAnswers);
        bAnswer = (Button) header.findViewById(R.id.bAnswerHeaderQueryAnswers);
        TextView tvTitle = header.findViewById(R.id.tvHeaderQueryAnswers);
        TextView placeName = header.findViewById(R.id.tvPlaceHeaderQueryAnswers);
        placeName.setVisibility(View.VISIBLE);
        placeName.setText(place);
        TextView datePosted = header.findViewById(R.id.tvDateHeaderQueryAnswers);
        datePosted.setVisibility(View.VISIBLE);
        Date datePost = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        datePosted.setText("Posted on "+sdf.format(datePost));
        askedUser.setVisibility(View.GONE);
        bAnswer.setVisibility(View.GONE);
        //askedUser.setText(userName);
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
        populateQueryAnswers = new PopulateQueryAnswers(QueryUserAnswers.this,answers,answersUsers,answersIds,answersUserIds,count);
        listView.addHeaderView(header);
        listView.setAdapter(populateQueryAnswers);

        bAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("queryId",queryId);
                Intent intent = new Intent("com.user.planb.POSTANSWER");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()){
/**
            case R.id.deleteQuery:
                if(checkConnection()) {
                    String id = Integer.toString(queryId);
                    RequestPackage requestPackage = new RequestPackage();
                    requestPackage.setMethod("POST");
                    requestPackage.setUri(getResources().getString(R.string.server) + "deleteUserQuery");
                    requestPackage.setParam("queryId", id);
                    QueryUserAnswers.DeleteQueries deleteQueries = new QueryUserAnswers.DeleteQueries();
                    deleteQueries.execute(requestPackage);

                }else {

                    Toast.makeText(QueryUserAnswers.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
                return true;
 */
            default:
                onBackPressed();
                return true;
        }

    }

    private boolean checkConnection() {
        Log.e("saireddy4","saireddy");
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_user_menu, menu);
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

    private class DeleteQueries extends AsyncTask<RequestPackage,String,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
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
                //progressBar.setVisibility(View.INVISIBLE);
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(QueryUserAnswers.this, "Cannot delete place. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {
                Log.e("xxxx11", "xx" + data);
                try {
                    dataItems = jsonParser.parseDeleteSelPlacesFeed(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("xxxx22", "xx" + dataItems.getDeleteSelPlacesStatus());
                if (dataItems.getDeleteSelPlacesStatus().equals("successful")) {
                    Toast.makeText(QueryUserAnswers.this, "Query deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QueryUserAnswers.this, "Cannot delete query. Please try after sometime", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}

