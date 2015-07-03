package com.hassan.masla7ty.fragments;

import android.content.Context;
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
import com.hassan.masla7ty.activities.LoginActivity;
import com.hassan.masla7ty.adapters.UserFriendsAdapter;
import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;

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

    private Context mcontext;



    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            "http://masla7ty.esy.es/app/getfriend_controller.php";
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

            friendsLists = new ArrayList<Friend>();

        }



        @Override
        protected Boolean doInBackground(Void... params)
        {


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("userName", LoginActivity.getUsername()));

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
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("Get_friends");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject friends = jsonArray.getJSONObject(i);

                        Friend friend = new Friend
                                (
                                        friends.getString("id"),
                                        friends.getString("First_name"),
                                        friends.getString("Last_name"),
                                        friends.getInt("status"),
                                        friends.getString("user_image")



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

            if (aBoolean)
            {
                mUserFriendsAdapter = new UserFriendsAdapter(getActivity(),
                        friendsLists);
                mRecyclerView.setAdapter(mUserFriendsAdapter);
            }


            else {

                friendsLists.add(new Friend("hassan@gmail.com","Hassan","ramadan",1,"http://masla7ty.esy.es/app/uploads/631420-06-1510382635_676632562451415_5481565986969569974_n.jpg"));
                friendsLists.add(new Friend("hassan@gmail.com","Hassan","ramadan",0,"http://masla7ty.esy.es/app/uploads/922120-06-1510382635_676632562451415_5481565986969569974_n.jpg"));
                friendsLists.add(new Friend("hassan@gmail.com","Hassan","ramadan",1,"http://masla7ty.esy.es/app/uploads/631420-06-1510382635_676632562451415_5481565986969569974_n.jpg"));
                friendsLists.add(new Friend("hassan@gmail.com","Hassan","ramadan",0,"http://masla7ty.esy.es/app/uploads/922120-06-1510382635_676632562451415_5481565986969569974_n.jpg"));
                friendsLists.add(new Friend("hassan@gmail.com","Hassan","ramadan",1,"http://masla7ty.esy.es/app/uploads/631420-06-1510382635_676632562451415_5481565986969569974_n.jpg"));
                mUserFriendsAdapter = new UserFriendsAdapter(getActivity(),
                        friendsLists);
                mRecyclerView.setAdapter(mUserFriendsAdapter);

                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
