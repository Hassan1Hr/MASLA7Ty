package com.hassan.masla7ty.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hassan.masla7ty.mainclasses.JSONParser;
import com.hassan.masla7ty.mainclasses.Post;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.PostAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyProfile extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;
    String Username;
    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            ApplicationURL.appDomain.concat("myPosts.php");

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("My Profile");
        mRecyclerView =(RecyclerView) findViewById(R.id.userPostRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager

        mRecyclerView.setLayoutManager(layoutManager);
        SharedPreferences sharedPref =getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
        Username= sharedPref.getString("username",null);
       // Toast.makeText(this,Username,Toast.LENGTH_LONG).show();
        new GetPostTask().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
    public class GetPostTask extends AsyncTask<Void, Void, Boolean>
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
            pairs.add(new BasicNameValuePair("username",Username));


            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);

            if (jsonObjectResult == null)
            {
                error = getBaseContext().getResources().getString(R.string.error);
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
                    error =  getBaseContext().getResources().getString(R.string.no_post);

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
                        posts,Username);
                mRecyclerView.setAdapter(postAdapter);
            }
            else
                Toast.makeText(MyProfile.this, error, Toast.LENGTH_LONG).show();
        }
    }
}
