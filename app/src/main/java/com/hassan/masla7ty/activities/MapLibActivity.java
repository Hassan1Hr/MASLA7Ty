package com.hassan.masla7ty.activities;

import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Demo of map areasx
 *
 */
public class MapLibActivity extends ActionBarActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    public static double latitude;
    public static double longitude;
    public static double mRadius;
    private GoogleMap map;
    private MapAreaManager circleManager;
    private Random[] randomMarkers;
    private MarkerOptions[] options;
    private ArrayList<Marker> onlineUsers;
    private JSONParser jsonParser = new JSONParser();
    private String userName;
    private ArrayList<Friend> markers;
    private String GET_USERS_URL = "http://masla7ty.esy.es/app/getfriend_controller.php";
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
    private static final String API_KEY = "AIzaSyAENxKBJCbh2fmjH0ax5r96hUL4WCG3Dt0";
    private static final String API = (PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);


    HandlerThread mHandlerThread;
    Handler mThreadHandler;
    //private Address address;
    private LatLng latLng;
    private Marker mMarker;
    private static LatLng loc;
    private SearchView searchView;
    private DownloadTask usersDownloadTask;
    private String stringSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();

        toolbar = (Toolbar) findViewById(R.id.maptoolbar);
        setSupportActionBar(toolbar);
        /* **
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

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
        }); **/
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

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_map);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stringSearch = newText.toString();
                placesDownloadTask = new DownloadTask(PLACES);
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                String url = getAutoCompleteUrl(newText.toString());
                placesDownloadTask.execute(url);
                return true;
            }
        });
        return true;
    }

    private void drawMarkers(ArrayList<Friend> markers) {
        for (int i = 0; i < markers.size(); i++) {
            MarkerOptions options1 = new MarkerOptions()
                    .title(markers.get(i).getFirstName() + " " + markers.get(i).getLastName())
                    .position(new LatLng(markers.get(i).getLatitude(), markers.get(i).getLongitude()));
            map.addMarker(options1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
                setupMap();
                new GetOnlineUsersTask().execute();
            }
        }
        map.setMyLocationEnabled(true);
    }

    private void setupMap() {
        circleManager = new MapAreaManager(map,

                4, Color.RED, Color.HSVToColor(70, new float[]{1, 1, 200}), //styling

                R.drawable.move, R.drawable.resize, //custom drawables for move and resize icons

                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle

                new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)

                new MapAreaManager.CircleManagerListener() { //listener for all circle events

                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                        //Toast.makeText(MapLibActivity.this, "do something on drag end circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                        //calcUsersDist();
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
                        Toast.makeText(MapLibActivity.this, "do something on moved circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
                        //calcUsersDist();
                    }

                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                        Toast.makeText(MapLibActivity.this, "do something on move circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                        Toast.makeText(MapLibActivity.this, "do something on resize circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                        Toast.makeText(MapLibActivity.this, "do something on min radius: " + draggableCircle, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                        Toast.makeText(MapLibActivity.this, "do something on max radius: " + draggableCircle, Toast.LENGTH_LONG).show();
                    }
                });

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.223, 31.3339), 10));
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMarker = map.addMarker(new MarkerOptions().position(loc));
                if(map != null){
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.abc_ic_commit_search_api_mtrl_alpha);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setPosition(3)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .attachTo(actionButton)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

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
            jsonObjectResult = jsonParser.makeHttpRequest(GET_USERS_URL, null);

            if (jsonObjectResult == null) {
                Toast.makeText(getApplicationContext(),
                        "error map", Toast.LENGTH_LONG).show();

                //error = "Error int the connection";
                return false;
            }

            try {
                if (jsonObjectResult.getInt("success") == 1) {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("Get_friends");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject online_users = jsonArray.getJSONObject(i);
                        firstName = online_users.getString("First_name");
                        lastName = online_users.getString("Last_name");
                        lat = online_users.getDouble("latitude");
                        longi = online_users.getDouble("longitude");
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
                Toast.makeText(getApplicationContext(), "error in map load", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

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
    private class ParserTask extends AsyncTask<String, Integer, MatrixCursor> {

        int parserType = 0;
        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected MatrixCursor doInBackground(String... jsonData) {

            JSONObject jObject;
            MatrixCursor list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
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
        protected void onPostExecute(MatrixCursor result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};
                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), android.R.layout.simple_list_item_1, result, from, to);
                    searchView.setSuggestionsAdapter(adapter);
                    break;
                case PLACES_DETAILS:

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(String.valueOf(result.getColumnIndex("lat")));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(String.valueOf(result.getColumnIndex("lng")));

                    // Getting reference to the SupportMapFragment of the activity_main.xml
                    SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    // Getting GoogleMap from SupportMapFragment
                    googleMap = fm.getMap();


                    LatLng point = new LatLng(latitude, longitude);

                    CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                    CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(2);

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