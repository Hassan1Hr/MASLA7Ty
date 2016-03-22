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

import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.FriendAdapters;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFriends extends AppCompatActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private FriendAdapters friendAdapters;
    private ArrayList<Friend> friendsLists;
    String Username;
    private JSONParser jsonParser = new JSONParser();

    private String READFRIEND_URL = ApplicationURL.appDomain.concat("myFriends.php");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("My Friends");
        mRecyclerView =(RecyclerView) findViewById(R.id.myFriendsRecyclerView);
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
        new GetFriendsTask().execute();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    public  class GetFriendsTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            friendsLists = new ArrayList<Friend>();

        }



        @Override
        protected Boolean doInBackground(Void... params)
        {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username",Username));

            jsonObjectResult = jsonParser.makeHttpRequest(READFRIEND_URL, pairs);

            if (jsonObjectResult == null)
            {
                error = "Error in the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("myFriends");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject friends = jsonArray.getJSONObject(i);

                        Friend friendsList = new Friend
                                (
                                        friends.getString("username"),
                                        friends.getString("firstName"),
                                        friends.getString("lastName"),
                                        friends.getInt("status"),
                                        friends.getString("userImage")


                                );
                        friendsLists.add(friendsList);
                    }
                    return true;
                }
                else
                    error = "No Friends";

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
                friendAdapters = new FriendAdapters(MyFriends.this,
                        friendsLists);
                mRecyclerView.setAdapter(friendAdapters);
            }


            else {



                Toast.makeText(MyApplication.getInstance(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
