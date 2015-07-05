package com.hassan.masla7ty.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hassan.masla7ty.R;

import com.hassan.masla7ty.adapters.ServiceAdapter;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.MainClasses.Service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 5/8/2015.
 */
public class ServiceFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private ServiceAdapter serviceAdapter;
    private ArrayList<Service> mservice;
    private ArrayList<Service> dummlist;

    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            "http://masla7tyfinal.esy.es/app/enterprisesAroundYou.php";





    public static ServiceFragment newInstance() {

        ServiceFragment fragment = new ServiceFragment();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        mRecyclerView =(RecyclerView) view.findViewById(R.id.postRecyclerView);
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);
        new GetServiceTask().execute();
        return view;
    }
    private class GetServiceTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mservice = new ArrayList<Service>();
            dummlist = new ArrayList<Service>();

        }
        @Override
        protected Boolean doInBackground(Void... params)
        {
            double latitude = 27.18858;
            double longitude = 31.181273;


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("radius",15+""));
            pairs.add(new BasicNameValuePair("latitude",latitude+""));
            pairs.add(new BasicNameValuePair("longitude",longitude+""));



            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);
            if (jsonObjectResult == null)
            {
                error = "Error int the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("enterprises");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject services = jsonArray.getJSONObject(i);

                        Service service = new Service
                                (
                                        services.getString("enterpriseEmail"),
                                        services.getString("enterpriseName"),

                                        services.getString("profilePicture"),
                                        services.getString("distance")
                                       

                                );
                        mservice.add(service);
                    }
                    return true;
                }
                else
                    error = jsonObjectResult.getString("message");

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
                serviceAdapter = new ServiceAdapter(getActivity(),
                        mservice);
                mRecyclerView.setAdapter(serviceAdapter);
            }
            else{
                Service service = new Service("Assiut Sation","enterpriseAddress","","distance");
                dummlist.add(service);

                serviceAdapter = new ServiceAdapter(getActivity(),
                        dummlist);
                mRecyclerView.setAdapter(serviceAdapter);

            }
               // Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }


}