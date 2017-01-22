package com.hassan.masla7ty.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.LoginActivity;
import com.hassan.masla7ty.activities.UserProfileActivity;
import com.hassan.masla7ty.adapters.UserFriendsAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 6/25/2015.
 */
public class UserFriendsFragement extends Fragment {
    private RecyclerView mRecyclerView;
    private UserFriendsAdapter mUserFriendsAdapter;
    private ArrayList<Friend> friendsLists;
    private ProgressBar myFriendProgress;
    private Context mcontext;



    private JSONParser jsonParser = new JSONParser();

    private String USER_FRIENDS_URL =
            ApplicationURL.appDomain+"userFriends.php";
    public static final String ARG_PAGE = "ARG_PAGE";



    public static UserFriendsFragement newInstance() {

        UserFriendsFragement fragment = new UserFriendsFragement();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendrecyclerview, container, false);
        myFriendProgress = (ProgressBar)view.findViewById(R.id.friendProgress);
        mRecyclerView =(RecyclerView) view.findViewById(R.id.friendRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        // Bind adapter to recycler
        new GetUserFriendsTask().execute();
        return  view;
    }
    public  class GetUserFriendsTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            myFriendProgress.setVisibility(View.VISIBLE);

            friendsLists = new ArrayList<Friend>();

        }



        @Override
        protected Boolean doInBackground(Void... params)
        {

            SharedPreferences sharedPref =getActivity().getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
           String  Username= sharedPref.getString("username",null);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("userOne", Username));
            pairs.add(new BasicNameValuePair("userTwo", UserProfileActivity.username));
            jsonObjectResult = jsonParser.makeHttpRequest(USER_FRIENDS_URL, pairs);

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

                        Friend friend = new Friend
                                (
                                        friends.getString("username"),
                                        friends.getString("firstName"),
                                        friends.getString("lastName"),
                                        friends.getInt("status"),
                                        friends.getString("profilePicture")



                                );
                        friendsLists.add(friend);
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
            myFriendProgress.setVisibility(View.GONE);

            if (aBoolean)
            {
                mUserFriendsAdapter = new UserFriendsAdapter(getActivity(),
                        friendsLists);
                mRecyclerView.setAdapter(mUserFriendsAdapter);
            }


            else {



                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
