package com.user.planb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.user.planb.ParserAndUtility.JsonParser;
import com.user.planb.database.DataSource;
import com.user.planb.model.DataItems;
import com.user.planb.receiver.ConnectivityReceiver;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 9/9/2017.
 */

public class PostComment extends AppCompatActivity {
    Toolbar toolbar;
    EditText etpostdetails;
    ImageButton ibhyperlink,ibattachimage;
    int RESULT_LOAD_IMG=1;
    LinearLayout focusText,buttonVisible;
    ImageButton boldButton,italicButton,underlineButton;
    Button bDone,bToolbar;
    ProgressBar progressBar;
    String http_output;
    int answer_id;
    Map<String,String> data = new HashMap<>();
    int mStart=-1;
    int italicStart=-1;
    int underLineStart=-1;
    boolean bold=false;
    boolean italic = false;
    boolean underline = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_post);
        Bundle bundle = getIntent().getExtras();
        answer_id = bundle.getInt("answerId");
        toolbar = (Toolbar) findViewById(R.id.tbIncludePostAnswer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        etpostdetails = (EditText)findViewById(R.id.etEventPostAnswer);
        ibattachimage=(ImageButton)findViewById(R.id.ibImagePostAnswer);
        ibhyperlink = (ImageButton) findViewById(R.id.ibHyperlinkPostAnswer);
        boldButton = (ImageButton) findViewById(R.id.ibBoldPostAnswer);
        italicButton= (ImageButton) findViewById(R.id.ibItalicPostAnswer);
        underlineButton = (ImageButton) findViewById(R.id.ibUnderlinePostAnswer);
        focusText = (LinearLayout)findViewById(R.id.llPostAnswer);
        buttonVisible = (LinearLayout)findViewById(R.id.trPostAnswer);
        progressBar = (ProgressBar) findViewById(R.id.pbPostAnswer);
        bToolbar = (Button) findViewById(R.id.bToolbarButton);
        bToolbar.setText("Done");

        focusText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etpostdetails.requestFocus();
                etpostdetails.setFocusableInTouchMode(true);
                buttonVisible.setVisibility(View.VISIBLE);
                return false;
            }
        });

        etpostdetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonVisible.setVisibility(View.VISIBLE);
                } else {
                    buttonVisible.setVisibility(View.GONE);
                }
            }
        });

        ibattachimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostComment.this);
                builder.setTitle(Html.fromHtml("<font color='#c24946'>Select Option</font>"));
                View view= getLayoutInflater().inflate(R.layout.upload_options,null);
                Button pickImage = (Button)view.findViewById(R.id.bGalleryOptionsUpload);
                Button pickCamera = (Button)view.findViewById(R.id.bCameraOptionsUpload);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog= builder.create();
                pickCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 100);
                    }
                });
                pickImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                        startActivityForResult(i, RESULT_LOAD_IMG);


                    }
                });

                dialog.setView(view);
                dialog.show();

            }


        });

        etpostdetails.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int currentLoc = etpostdetails.getSelectionStart();


                if (bold == true) {

                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn = (Spannable) etpostdetails.getText();
                    Log.e("pradeepstarterror", "not bold end" + end);
                    if (end > 0) {
                        spn.setSpan(new StyleSpan(Typeface.BOLD), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                } else if (bold == false) {

                    if (mStart == -1) mStart = 0;
                    int end = etpostdetails.getSelectionEnd();
                    Log.e("pradeepstarterror", "not bold end" + end);
                    Spannable spn2 = (Spannable) etpostdetails.getText();
                    if (end > 0) {
                        spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                if (italic == true) {

                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn = (Spannable) etpostdetails.getText();
                    if (end > 0) {
                        spn.setSpan(new StyleSpan(Typeface.ITALIC), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (italic == false) {

                    if (italicStart == -1) italicStart = 0;
                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn2 = (Spannable) etpostdetails.getText();
                    if (end > 0) {
                        spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }


                if (underline == true) {

                    int end = etpostdetails.getSelectionEnd();
                    UnderlineSpan[] ss = s.getSpans(end - 1, end, UnderlineSpan.class);
                    Spannable spn = (Spannable) etpostdetails.getText();
                    if (end > 0) {
                        spn.setSpan(new UnderlineSpan(), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (underline == false) {
                    underlineButton.setImageResource(R.drawable.ic_format_underline);
                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn2 = (Spannable) etpostdetails.getText();
                    if (end > 0) {
                        spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //unused
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //unused


            }

        });

        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bold == false) {
                    //mStart = Selection.getSelectionStart(etaskquery.getText());
                    boldButton.setImageResource(R.drawable.ic_format_bold_primary);
                    bold = true;
                } else {
                    boldButton.setImageResource(R.drawable.ic_format_bold);
                    bold = false;
                    //mStart = Selection.getSelectionStart(etaskquery.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (italic==false) {
                    italicButton.setImageResource(R.drawable.ic_format_italic_primary);
                    //italicStart = Selection.getSelectionStart(etaskquery.getText());
                    italic=true;
                } else {
                    italicButton.setImageResource(R.drawable.ic_format_italic);
                    italic=false;
                    //italicStart = Selection.getSelectionStart(etaskquery.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (underline == false) {
                    underlineButton.setImageResource(R.drawable.ic_format_underline_primary);
                    underLineStart = Selection.getSelectionStart(etpostdetails.getText());
                    underline = true;
                } else {
                    underline = false;
                    underLineStart = Selection.getSelectionStart(etpostdetails.getText());
                    underlineButton.setImageResource(R.drawable.ic_format_underline);
                    //mStart = etaskquery.getText().length();
                }
            }
        });

        bToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }

                SessionManagement sessionManagement = new SessionManagement(PostComment.this);
                int user_id = sessionManagement.getUserId();
                String description = etpostdetails.getText().toString();
                String userId = Integer.toString(user_id);
                String answerId = Integer.toString(answer_id);
                if (description.equals("")) {
                    Toast.makeText(PostComment.this, "Comment is empty. Please add comment", Toast.LENGTH_LONG).show();
                } else {
                    if (checkConnection()) {
                        try {
                            RequestPackage requestPackage = new RequestPackage();
                            requestPackage.setMethod("POST");
                            requestPackage.setUri(getResources().getString(R.string.server) + "answerComments");
                            requestPackage.setParam("userId", userId);
                            requestPackage.setParam("comment", description);
                            requestPackage.setParam("answerId", answerId);
                            PostComment.AddComment addComment = new PostComment.AddComment();
                            addComment.execute(requestPackage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PostComment.this, "No Internet connect. Please connect and try again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    private void addImageBetweentext(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor, "\n.");

        selectionCursor = etpostdetails.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(etpostdetails.getText());
        Log.e("pradeepstarterror",selectionCursor+"selectioncursor");
        builder.setSpan(new ImageSpan(drawable),selectionCursor-1, selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etpostdetails.setText(builder);
        etpostdetails.setSelection(selectionCursor);
        int selectionline = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionline, "\n");
    }

    private void addHyperlink(EditText editText){

        String hyperlink ="<ahref='"+editText.getText().toString()+"'>"+editText.getText().toString()+"</a" ;
        int selectionCursor = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor, " ");
        int selectionCursor1 = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor1, Html.fromHtml(hyperlink));
        int selectionline = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionline, " ");

        selectionCursor = etpostdetails.getSelectionStart();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            //Drawable drawable=imageGetter.getDrawable(imgDecodableString);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(imagePath);
            Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage, 600, 400, false);

            Drawable drawable =new BitmapDrawable(getResources(),bt);
            addImageBetweentext(drawable);


        }
        else if(requestCode == 100 && resultCode== RESULT_OK && data != null){

            //Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage,600,400,false);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap bt = Bitmap.createScaledBitmap(photo,600,400,false);
            Drawable drawable =new BitmapDrawable(getResources(),bt);
            Log.e("pradeepimage2","pradeep");
            addImageBetweentext(drawable);
        }

    }

    Html.ImageGetter imageGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String filePath) {
            Drawable d = Drawable.createFromPath(filePath);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    };
    private class AddComment extends AsyncTask<RequestPackage,String,String> {

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
            Log.e("pradeepplanb","pradeepplanb"+content);
            JsonParser jsonParser = new JsonParser();
            if(http_output == null){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostComment.this, "Maintainence activity is in progress. Please try after sometime", Toast.LENGTH_LONG).show();
            }else {
                try {
                    dataItems = jsonParser.parseQueryPostFeed(http_output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataItems.getQueryStatus().equals("successful")) {

                    DataSource source = new DataSource(PostComment.this);
                    progressBar.setVisibility(View.INVISIBLE);
                    setResult(Activity.RESULT_OK);
                    finish();
                    Toast.makeText(PostComment.this, "Your comment has been succesfully posted. " , Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(PostComment.this, "Cannot Post. Try after sometime. "
                            , Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
