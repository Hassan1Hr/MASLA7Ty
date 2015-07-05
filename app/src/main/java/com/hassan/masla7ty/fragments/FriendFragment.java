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
import android.widget.Toast;

import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.MainActivity;
import com.hassan.masla7ty.activities.MyFriends;
import com.hassan.masla7ty.activities.MyProfile;
import com.hassan.masla7ty.adapters.FriendAdapters;
import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.pojo.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 3/23/2015.
 */
public class FriendFragment extends Fragment {
    //private ListView mListView;
    private RecyclerView mRecyclerView;
    private FriendAdapters friendAdapters;
    private ArrayList<Friend> friendsLists;
    private ArrayList<Friend> dummlist;
    private Context mcontext;
    List<NameValuePair> pairs;


    private JSONParser jsonParser = new JSONParser();

    private String READFRIEND_URL;
    public static final String ARG_PAGE = "ARG_PAGE";

    private String Username;

    public static FriendFragment newInstance() {

        FriendFragment fragment = new FriendFragment();

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
        mRecyclerView =(RecyclerView) view.findViewById(R.id.friendRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);


        if (getActivity() instanceof MainActivity){
            double latitude = 27.200243;
            double longitude = 31.182949;
            READFRIEND_URL =
            "http://masla7tyfinal.esy.es//app/friendsAroundYou.php";
            pairs= new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("latitude",latitude+""));
            pairs.add(new BasicNameValuePair("longitude",longitude+""));
            new GetFriendsTask().execute();

        }
        else if (getActivity() instanceof MyFriends){
            String  defaultUser  = "hassan@gmail.com";
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            Username= sharedPref.getString("username",defaultUser);
            pairs= new ArrayList<NameValuePair>();
            //pairs.add(new BasicNameValuePair("username",Username));
            pairs.add(new BasicNameValuePair("latitude",27.200243+""));
            pairs.add(new BasicNameValuePair("longitude",31.182949+""));
            READFRIEND_URL=
            "http://masla7tyfinal.esy.es//app/friendsAroundYou.php";
            new GetFriendsTask().execute();
        }
        return  view;
    }



    public  class GetFriendsTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dummlist =new ArrayList<Friend>();
            friendsLists = new ArrayList<Friend>();

        }



        @Override
        protected Boolean doInBackground(Void... params)
        {




            jsonObjectResult = jsonParser.makeHttpRequest(READFRIEND_URL, null);

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

                                        friends.getString("profilePicture")


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
                friendAdapters = new FriendAdapters(MyApplication.getAppContext(),
                        friendsLists);
                mRecyclerView.setAdapter(friendAdapters);
            }


            else {



                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }

}