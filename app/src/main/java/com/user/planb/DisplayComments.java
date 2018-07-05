package com.user.planb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.user.planb.model.DataItems;

import java.util.ArrayList;

/**
 * Created by user on 4/8/2018.
 */

public class DisplayComments extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_display);
        DataSeralize dataSeralize = (DataSeralize) getIntent().getSerializableExtra("serialize_data");
        ArrayList<DataItems> commentsList = dataSeralize.getComments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbDisplayComments);
        ImageButton imageButton = (ImageButton) findViewById(R.id.ibToolbarClose);
        toolbar.setTitle("Comments");
        ListView listView = (ListView) findViewById(R.id.lvDisplayComments);
        PopulateComments populateComments = new PopulateComments(DisplayComments.this,commentsList);

        listView.setAdapter(populateComments);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private class PopulateComments extends ArrayAdapter {

        ArrayList<DataItems> commentsList = new ArrayList<>();
        LayoutInflater li;
        public PopulateComments(Activity activity, ArrayList<DataItems> commentsList) {
            super(activity, R.layout.comments_populate,commentsList);
            li = activity.getWindow().getLayoutInflater();
            this.commentsList = commentsList;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DataItems comments = commentsList.get(position);

            String comment = comments.getComment();
            Integer commentId = comments.getCommentId();
            String commentUser = comments.getCommentUser();
            Integer commentUserId = comments.getCommentUserId();

            convertView = li.inflate(R.layout.comments_populate, parent, false);
            TextView tvCommentUser = (TextView) convertView.findViewById(R.id.tvNamePopulateComments);
            ExpandableTextView etvComment = (ExpandableTextView) convertView.findViewById(R.id.etvDescriptionPopulateComments);
            tvCommentUser.setText(commentUser);
            etvComment.setText(comment);
            return convertView;
        }
    }

}
