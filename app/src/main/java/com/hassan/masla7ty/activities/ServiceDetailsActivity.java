package com.hassan.masla7ty.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.ApplicationURL;

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
    private LruCache<String,Bitmap> imageCache;
    private RequestQueue imagequeue;
    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            ApplicationURL.appDomain.concat("aboutService.php");
    final int maxMemory;
    final int cacheSize;

    public ServiceDetailsActivity() {
        maxMemory= (int)(Runtime.getRuntime().maxMemory()/1024);
        cacheSize = maxMemory/8;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        imageCache = new LruCache<>(cacheSize);
        imagequeue = Volley.newRequestQueue(getApplicationContext());
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        serviceName = intent.getStringExtra("serviceName");

        serviceDistance = intent.getStringExtra("serviceDistance");
        serviceDetailsView = (TextView)findViewById(R.id.service_details);
        serviceDistanceView = (TextView)findViewById(R.id.service_distance2);
        serviceNameView = (TextView)findViewById(R.id.service_name2);
        serviceImage = (com.pkmmte.view.CircularImageView)findViewById(R.id.service_image2);
        serviceNameView.setText(serviceName);
        serviceDistanceView.setText(serviceDistance);
        new GetServiceDetails().execute();


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
            pairs.add(new BasicNameValuePair("username",serviceId));


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
                    serviceUrl = jsonObjectResult.getString("serviceImage");
                    serviceDetails =  jsonObjectResult.getString("serviceDetails");
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
                if((serviceUrl != null)&& (serviceUrl != "")) {


                    Bitmap userPhoto = imageCache.get(serviceId);
                    if((serviceUrl!="") && (serviceUrl!=null)) {
                        if (userPhoto != null) {
                            serviceImage.setImageBitmap(userPhoto);
                        } else {

                            ImageRequest request = new ImageRequest(serviceUrl,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            serviceImage.setImageBitmap(bitmap);
                                            imageCache.put(serviceId, bitmap);
                                        }
                                    },
                                    80, 80,
                                    Bitmap.Config.ARGB_8888,
                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Log.d("userImage ", volleyError.getMessage());
                                        }
                                    }
                            );
                            imagequeue.add(request);
                        }
                    }
                }

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
