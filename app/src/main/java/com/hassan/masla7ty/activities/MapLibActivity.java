package com.hassan.masla7ty.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.map.MapAreaManager;
import com.hassan.masla7ty.map.MapAreaMeasure;
import com.hassan.masla7ty.map.MapAreaWrapper;
import com.hassan.masla7ty.places.PlaceDetailsJSONParser;
import com.hassan.masla7ty.places.PlaceJSONParser;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Demo of map areasx
 *
 */
public class MapLibActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    public static double latitude;
    public static double longitude;
    public static double mRadius;
    private MapAreaManager circleManager;
    private Random[] randomMarkers;
    private MarkerOptions[] options;
    private ArrayList<Marker> onlineUsers;
    private JSONParser jsonParser = new JSONParser();
    private String userName;
    private ArrayList<Friend> markers;
    private String GET_USERS_URL = ApplicationURL.appDomain.concat("friendsAroundYou.php");
    private double lat;
    private double longi;
    private String firstName;
    private String lastName;

    AutoCompleteTextView atvPlaces;

    DownloadTask placesDownloadTask;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;

    GoogleMap googleMap;

    final int PLACES = 0;
    final int PLACES_DETAILS = 1;


    private static final String TAG = "google";
    private static final String LOG_TAG = "SearchViewActivity1";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //private static final String API_KEY = "AIzaSyAENxKBJCbh2fmjH0ax5r96hUL4WCG3Dt0";
    private static final String API = (PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);


    HandlerThread mHandlerThread;
    Handler mThreadHandler;
    //private Address address;
    private LatLng latLng;
    private Marker mMarker;
    private static LatLng loc;
    private MapView mapView;
    private View btnView;
    private ArrayList<Parcelable> pointList;
    private String url;
    private ImageView parsedIcon;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView  =(MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAENxKBJCbh2fmjH0ax5r96hUL4WCG3Dt0";

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        String language = "ar";
        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key + "&" + language;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/queryautocomplete/" + output + "?" + parameters;

        return url;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAENxKBJCbh2fmjH0ax5r96hUL4WCG3Dt0";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }


    /**
     * A method to download json data from url
     */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }


    private void drawMarkers(ArrayList<Friend> markers) {

        //v = getLayoutInflater().inflate(R.layout.custom_marker_layout,
        //        null);
        //Bitmap output = createDrawableFromView(MapLibActivity.this,v);
        //ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        for (int i = 0; i < markers.size(); i++) {
            //Glide.with(MapLibActivity.this)
            //        .load(markers.get(i).getUrl())
            //        .into(imageView);
            MarkerOptions options1 = new MarkerOptions()
                    .title(markers.get(i).getFirstName() + " " + markers.get(i).getLastName())
                    //        .icon(BitmapDescriptorFactory.fromBitmap(output))
                    .position(new LatLng(markers.get(i).getLatitude(), markers.get(i).getLongitude()));
            googleMap.addMarker(options1);
        }
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /*private Bitmap loadImageForMarker() {
        Glide.with(MapLibActivity.this)
                .load(url)
                .into(i);

        Bitmap userPhoto = imageCache.get(postData.getPostId());
        if(userPhoto != null)
        {
            holder.UserImage.setImageBitmap(userPhoto);
        } else {
            String userURl = postData.getUserPhoto();
            ImageRequest request = new ImageRequest(userURl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.UserImage.setImageBitmap(bitmap);
                            imageCache.put(postData.getPostId(), bitmap);
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
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void setupMap() {
        circleManager = new MapAreaManager(googleMap,

                4, Color.RED, Color.HSVToColor(70, new float[]{1, 1, 200}), //styling

                R.drawable.move, R.drawable.resize, //custom drawables for move and resize icons

                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle

                new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)

                new MapAreaManager.CircleManagerListener() { //listener for all circle events

                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {

                        mRadius = draggableCircle.getRadius();
                        LatLng latLng = draggableCircle.getCenter();
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                    }

                    @Override
                    public void onCreateCircle(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on crate circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                        //calcUsersDist();
                    }

                    @Override
                    public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on moved circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                        //calcUsersDist();
                    }

                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on move circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on resize circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on min radius: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on max radius: " + draggableCircle, Toast.LENGTH_LONG).show();
                    }
                });
        double lat = 27.18090;//MainActivity.mCurrentLocation.getLatitude();
        double lng = 31.165726;//MainActivity.mCurrentLocation.getLongitude();
        if(lat != 0 && lng != 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,  lng), 14.0f));
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.18090,  31.165726), 15.0f));
        }

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.223, 31.3339), 10));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_add_location_black_24dp);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setPosition(6)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .attachTo(actionButton)
                .build();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("mCurrentLocation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("Longitude", Double.doubleToLongBits(longitude));
                editor.putLong("Latitude", Double.doubleToLongBits(latitude));
                editor.putFloat("radius", (float) mRadius);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Current Location has been saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       this.googleMap = googleMap;
       if(googleMap != null)
       {
        setupMap();
        new GetOnlineUsersTask().execute();
    }

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
    }
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        /*atvPlaces.setThreshold(1);

        // Adding textchange listener
        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {

                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);

            }
        });
*/
    //map.setRotateGesturesEnabled(true);

    View locationButton = ((View)findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
    // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    private class GetOnlineUsersTask extends AsyncTask<Void, Void, Boolean> {
        private JSONObject jsonObjectResult = null;
        private String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            markers = new ArrayList<Friend>();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("latitude", 27.18090+""));
            pairs.add(new BasicNameValuePair("longitude", 31.165726+""));
            pairs.add(new BasicNameValuePair("radius", 1+""));
            pairs.add(new BasicNameValuePair("username", "hassan@gmail.com"));
            jsonObjectResult = jsonParser.makeHttpRequest(GET_USERS_URL, pairs);
            if (jsonObjectResult == null) {
                //Toast.makeText(getApplicationContext(),
                 //       "error map", Toast.LENGTH_LONG).show();

                //error = "Error int the connection";
                return false;
            }

            try {
                if (jsonObjectResult.getInt("success") == 1) {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("myFriends");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject online_users = jsonArray.getJSONObject(i);
                        firstName = online_users.getString("firstName");
                        lastName = online_users.getString("lastName");
                        lat = online_users.getDouble("latitude");
                        longi = online_users.getDouble("longitude");
                        //url = online_users.getString("profilePicture");

                        //Friend friend = new Friend(firstName, lastName, lat, longi, url);
                        Friend friend = new Friend(firstName, lastName, lat, longi);

                        markers.add(friend);
                    }
                    return true;
                } else
                    error = "error map1";//jsonObjectResult.getString("message");

            } catch (Exception ex) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                drawMarkers(markers);
            } else {
                //Toast.makeText(getApplicationContext(), "error in map load", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }
    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};
                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
                    // Setting the adapter
                    atvPlaces.setAdapter(adapter);
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    // Getting reference to the SupportMapFragment of the activity_main.xml


                    LatLng point = new LatLng(latitude, longitude);

                    CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                    CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(1);

                    // Showing the user input location in the Google Map
                    googleMap.moveCamera(cameraPosition);
                    googleMap.animateCamera(cameraZoom);

                    MarkerOptions options = new MarkerOptions();
                    options.position(point);
                    options.title("Position");
                    options.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

                    // Adding the marker in the Google Map
                    googleMap.addMarker(options);
                    break;
            }
        }
    }
}