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

import com.hassan.masla7ty.mainclasses.JSONParser;
import com.hassan.masla7ty.mainclasses.Post;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.MainActivity;
import com.hassan.masla7ty.activities.UserProfileActivity;
import com.hassan.masla7ty.adapters.PostAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
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
public class PostFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;
    double latitude;
    double longitude ;
    double radiuse;
    private JSONParser jsonParser = new JSONParser();
    String username;
    private ProgressBar postProgress;
    private String READNEWS_URL =
            ApplicationURL.appDomain+"postsAroundYout.php";


    public static PostFragment newInstance() {

        PostFragment fragment = new PostFragment();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref =MyApplication.getInstance().getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
        username= sharedPref.getString("username", null);
        SharedPreferences locationSharedPref =getActivity().getSharedPreferences(MyApplication.UserLocationPrefernce, Context.MODE_PRIVATE);

         latitude =locationSharedPref.getFloat("Latitude", (float) 27.185875);
        longitude =locationSharedPref.getFloat("Longitude", (float) 31.168594);
         radiuse =locationSharedPref.getFloat("radius", (float) 15);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        postProgress = (ProgressBar)view.findViewById(R.id.postProgress);
        mRecyclerView =(RecyclerView) view.findViewById(R.id.postRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        if (getActivity() instanceof MainActivity)
        {new GetNewsTask().execute();}
        else
        {
            READNEWS_URL =
                    ApplicationURL.appDomain+"myPosts.php";
            new GetUserPostTask().execute();
        }

        return view;
    }
    public class GetNewsTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;



        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            posts = new ArrayList<Post>();
            postProgress.setVisibility(View.VISIBLE);


        }


        @Override
        protected Boolean doInBackground(Void... params)
        {


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("latitude",latitude+""));
            pairs.add(new BasicNameValuePair("longitude",longitude+""));
            pairs.add(new BasicNameValuePair("username",username+""));



            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);

            if (jsonObjectResult == null)
            {
                error =MyApplication.getAppContext().getResources().getString(R.string.error);
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
                    error = getResources().getString(R.string.success);;

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
            postProgress.setVisibility(View.GONE);

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
    public class GetUserPostTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            posts = new ArrayList<Post>();
            postProgress.setVisibility(View.VISIBLE);


        }


        @Override
        protected Boolean doInBackground(Void... params)
        {




            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", UserProfileActivity.username));

            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);

            if (jsonObjectResult == null)
            {
                error = getContext().getResources().getString(R.string.error);
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
            postProgress.setVisibility(View.GONE);

            if (aBoolean)
            {
                postAdapter = new PostAdapter(MyApplication.getAppContext(),
                        posts,username);
                mRecyclerView.setAdapter(postAdapter);
            }
            else
                Toast.makeText(MyApplication.getInstance(), error.toString(), Toast.LENGTH_LONG).show();
        }
    }
}