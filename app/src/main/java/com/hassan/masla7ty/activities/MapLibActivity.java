package com.hassan.masla7ty.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.map.MapAreaManager;
import com.hassan.masla7ty.map.MapAreaMeasure;
import com.hassan.masla7ty.map.MapAreaWrapper;
import com.hassan.masla7ty.map.MapAreasUtils;
import com.hassan.masla7ty.MainClasses.Friend;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Demo of map areasx
 *
 */
public class MapLibActivity extends ActionBarActivity implements OnMapReadyCallback {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //initMap();
        toolbar = (Toolbar)findViewById(R.id.maptoolbar);
        setSupportActionBar(toolbar);
        setUpMapIfNeeded();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }



    private void drawMarkers(ArrayList<Friend> markers) {
        for (int i = 0; i < markers.size(); i++) {
            MarkerOptions options1 = new MarkerOptions()
                    .title(markers.get(i).getFirstName()+" " + markers.get(i).getLastName())
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
    }

    private void setupMap() {
        circleManager = new MapAreaManager(map,

                4, Color.RED, Color.HSVToColor(70, new float[] {1, 1, 200}), //styling

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
                        calcUsersDist();
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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.223, 31.3339), 12));
    }

    private void calcUsersDist() {

        double[] result = new double[3];
        for (int i = 0; i < result.length; i++) {
            result[i] = MapAreasUtils.toRadiusMeters(MapAreaManager.currentPoint, options[i].getPosition());

            if (result[i] <= MapAreaManager.currentRadius) {
                Toast.makeText(getApplicationContext(), "Markers in circle marker: " + i + 1, Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(getApplicationContext(), "RESULT: " + result[1]/1000, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ImageView imageView = new ImageView(this);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .attachTo(actionButton)
                .build();
    }

    private class GetOnlineUsersTask extends AsyncTask<Void, Void, Boolean>
    {
        private JSONObject jsonObjectResult = null;
        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            markers = new ArrayList<Friend>();

        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            jsonObjectResult = jsonParser.makeHttpRequest(GET_USERS_URL, null);

            if (jsonObjectResult == null)
            {
                Toast.makeText(getApplicationContext(),
                        "error map" ,Toast.LENGTH_LONG).show();

                //error = "Error int the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
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
                }
                else
                    error = "error map1";//jsonObjectResult.getString("message");

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
                drawMarkers(markers);
            }
            else {
                Toast.makeText(getApplicationContext(), "error in map load", Toast.LENGTH_LONG).show();
            }
        }
    }

}