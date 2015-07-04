package com.hassan.masla7ty.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFriends extends ActionBarActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private FriendAdapters friendAdapters;
    private ArrayList<Friend> friendsLists;
    private JSONParser jsonParser = new JSONParser();

    private String READFRIEND_URL ="http://masla7ty.esy.es/app/getfriend_controller.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("");
        mRecyclerView =(RecyclerView) findViewById(R.id.myFriendsRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);
        new GetFriendsTask().execute();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_friends, menu);
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



            double latitude = 27.19;
            double longitude = 27.2;

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("latitude",latitude+""));
            pairs.add(new BasicNameValuePair("longitude",longitude+""));
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
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("Get_friends");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject friends = jsonArray.getJSONObject(i);

                        Friend friendsList = new Friend
                                (
                                        friends.getString("id"),
                                        friends.getString("First_name"),
                                        friends.getString("Last_name"),
                                        friends.getInt("status"),

                                        friends.getString("user_image")


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



                Toast.makeText(MyFriends.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
