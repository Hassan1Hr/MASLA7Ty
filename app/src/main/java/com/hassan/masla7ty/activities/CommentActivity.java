package com.hassan.masla7ty.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hassan.masla7ty.MainClasses.FriendComment;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.CommentAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<FriendComment> commentlist;
    private ArrayList<FriendComment> dummlist;
    ImageButton sendComment ;
    EditText writeComment;
    String commentString;
    int postId;
    private JSONParser jsonParser = new JSONParser();
    private String GET_COMMENT = ApplicationURL.appDomain.concat("commentsOfPosts.php");
    String ADD_COMMENt = ApplicationURL.appDomain.concat("addComment.php");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent().getIntExtra("postId", postId);
        setContentView(R.layout.activity_comment);

        sendComment = (ImageButton)findViewById(R.id.sendcomment);
        writeComment = (EditText)findViewById(R.id.writecomment);
        mRecyclerView =(RecyclerView)findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        dummlist =new ArrayList<FriendComment>();
        mRecyclerView.setLayoutManager(layoutManager);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment.setBackgroundColor(Color.BLUE);
                commentString = writeComment.getText().toString();
                if (TextUtils.isEmpty(commentString))
                {
                    writeComment.setError(getString(R.string.error_empty_field));
                }
                else {

                    new SendComment().execute();
                    sendComment.setBackgroundColor(Color.WHITE);
                    writeComment.setText("");
                }
            }
        });
        new GetComment().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  class GetComment extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            commentlist = new ArrayList<FriendComment>();

        }



        @Override
        protected Boolean doInBackground(Void... params)
        {


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("postId",postId+""));


            jsonObjectResult = jsonParser.makeHttpRequest(GET_COMMENT, pairs);

            if (jsonObjectResult == null)
            {
                error = "Error in the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("comments");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject comments = jsonArray.getJSONObject(i);

                        FriendComment comment = new FriendComment(
                                comments.getString("creatorId"),
                                comments.getString("firstName"),
                                comments.getString("lastName"),
                                comments.getString("profilePicture"),
                                comments.getString("commentDescription"),
                                comments.getString("commentDate"),
                                comments.getString("commentTime")
                        );
                        commentlist.add(comment);
                    }
                    return true;
                }
                else
                    error = "No comments";

            }
            catch (Exception ex)
            {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);

            if (aBoolean)
            {
                commentAdapter = new CommentAdapter(CommentActivity.this,
                        commentlist);
                mRecyclerView.setAdapter(commentAdapter);
            }


            else {
                FriendComment comment1 = new FriendComment(
                        "hassan@gmail.com",
                        "Ahmed",
                        "taha",
                        "http://masla7ty.esy.es/app/uploads/922120-06-1510382635_676632562451415_5481565986969569974_n.jpg",
                        " welcom to assiut",
                        "2015-02-02",
                        "12:13"
                );

                dummlist.add(comment1);

                commentAdapter = new CommentAdapter(CommentActivity.this,
                        dummlist);
                mRecyclerView.setAdapter(commentAdapter);


            }
        }
    }
    public  class SendComment extends AsyncTask<Void, Void, Boolean> {


        private JSONObject jsonObjectResult = null;
        ProgressDialog mProgressDialog;
        private String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(CommentActivity.this,
                    "Processing...", "send comment", false, false);


        }


        @Override
        protected Boolean doInBackground(Void... params) {
            Long time;
            SharedPreferences sharedPref =getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
            String Username= sharedPref.getString("username", null);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            time = date.getTime();
            String dateString = dateFormat.format(date).toString();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("postId", postId + ""));
            pairs.add(new BasicNameValuePair("creatorId", Username));
            pairs.add(new BasicNameValuePair("commentDescription", commentString));
            pairs.add(new BasicNameValuePair("commentDate", dateString));
            pairs.add(new BasicNameValuePair("commentTime", time+""));


            jsonObjectResult = jsonParser.makeHttpRequest(ADD_COMMENt, pairs);

            if (jsonObjectResult == null) {
                error = "Error in the connection";
                return false;
            }

            try {
                if (jsonObjectResult.getInt("success") == 1) {
                    return true;

                } else
                    error = "error send";


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean) {
                new GetComment().execute();

            } else {
                Toast.makeText(CommentActivity.this, error, Toast.LENGTH_LONG).show();
                new GetComment().execute();
            }
        }
    }
}
