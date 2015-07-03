package com.hassan.masla7ty.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.MainClasses.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailsActivity extends ActionBarActivity {
    String serviceId;
    String serviceName;
    String serviceDistance;
    String serviceUrl;
    String serviceDetails;
    TextView serviceDistanceView;
    TextView serviceDetailsView;
    TextView serviceNameView;
    com.pkmmte.view.CircularImageView serviceImage;
    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            "http://masla7ty.esy.es/app/getfriend_controller.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        serviceName = intent.getStringExtra("serviceName");
        serviceUrl =  intent.getStringExtra("serviceURL");
        serviceDistance = intent.getStringExtra("serviceDistance");
        serviceDetailsView = (TextView)findViewById(R.id.service_details);
        serviceDistanceView = (TextView)findViewById(R.id.service_distance2);
        serviceNameView = (TextView)findViewById(R.id.service_name2);
        serviceImage = (com.pkmmte.view.CircularImageView)findViewById(R.id.service_image2);
        serviceNameView.setText(serviceName);
        serviceDistanceView.setText(serviceDistance);
        if(!(serviceUrl==null)&&! (serviceUrl=="")) {


            Glide.with(this)
                    .load(serviceUrl)
                    .into(serviceImage);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_details, menu);
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
    public  class GetServiceDetails extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();



        }



        @Override
        protected Boolean doInBackground(Void... params)
        {


            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("serviceId",serviceId+""));


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
                    JSONObject jsonObject = jsonObjectResult.getJSONObject("serviceDetails");
                    serviceDetails =  jsonObject.getString("serviceInfo");
                    return true;
                }
                else
                    error = "No details";

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
               serviceDetailsView.setText(serviceDetails);
            }


            else {
                serviceDetailsView.setText("failed to get data from the sitefailed to get data from the site" +
                        "failed to get data from the site" +
                        "failed to get data from the sitefailed to get data from the site" +
                        "failed to get data from the site" +
                        "vfailed to get data from the site ");
            }
                }
        }
}
