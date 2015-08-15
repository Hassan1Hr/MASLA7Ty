package com.hassan.masla7ty.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.MainClasses.Post;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.FriendAdapters;
import com.hassan.masla7ty.adapters.PostAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostSearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;
    private JSONParser jsonParser = new JSONParser();
    String username;
    private String READNEWS_URL =
            ApplicationURL.appDomain+"postSearch.php";
    double latitude;
    double longitude ;
    double radiuse;
    String search;
    EditText postEditText;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);
        postEditText = (EditText)findViewById(R.id.searchEditText);
        searchButton = (Button)findViewById(R.id.searchButton);
        postEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = postEditText.getText().toString();
                new SearchPostTask().execute();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref =MyApplication.getInstance().getSharedPreferences(LoginActivity.UsernamePrefernce, Context.MODE_PRIVATE);
        username= sharedPref.getString("username", null);
        SharedPreferences locationSharedPref =getSharedPreferences(MainActivity.UserLocationPrefernce, Context.MODE_PRIVATE);

        latitude =locationSharedPref.getFloat("Latitude", (float) 27.185875);
        longitude =locationSharedPref.getFloat("Longitude", (float) 31.168594);
        radiuse =locationSharedPref.getFloat("radius", (float) 15);
        mRecyclerView =(RecyclerView) findViewById(R.id.postSearchRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);
        postEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_search, menu);
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
        } else if (id == R.id.action_search_post) {

        }

        return super.onOptionsItemSelected(item);
    }
    public class SearchPostTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;



        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            posts = new ArrayList<Post>();


        }


        @Override
        protected Boolean doInBackground(Void... params)
        {


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("latitude",latitude+""));
            pairs.add(new BasicNameValuePair("longitude",longitude+""));
            pairs.add(new BasicNameValuePair("username",username+""));
            pairs.add(new BasicNameValuePair("target",search));



            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);

            if (jsonObjectResult == null)
            {
                error = "Error in the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("posts");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject news = jsonArray.getJSONObject(i);

                        Post post = new Post
                                (
                                        news.getInt("postId"),
                                        news.getString("firstName"),
                                        news.getString("lastName"),
                                        news.getString("postDescription"),
                                        news.getString("postDate"),
                                        news.getString("postTime"),
                                        news.getString("postPhoto"),
                                        news.getString("userImage"),
                                        news.getInt("numberOfLikes"),
                                        news.getString("like")

                                );
                        posts.add(post);
                    }
                    return true;
                }
                else
                    error = "success = 0";

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
                postAdapter = new PostAdapter(MyApplication.getInstance(),
                        posts,username);
                mRecyclerView.setAdapter(postAdapter);
            }
            else
                Toast.makeText(MyApplication.getInstance(), error, Toast.LENGTH_LONG).show();
        }
    }

}
