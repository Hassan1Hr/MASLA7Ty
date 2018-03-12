package com.hassan.masla7ty.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.MainClasses.SearchJSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.MainActivityViewPagerAdapter;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;
import com.hassan.masla7ty.views.SlidingTabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {


    protected static final String TAG = "location-settings";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final String ADD_PLACE_URL = ApplicationURL.appDomain.concat("visitedLocations.php");
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static LocationManager service;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;


    private JSONParser jsonParser = new JSONParser();
    private static final String USERS_SEARCH = ApplicationURL.appDomain.concat("search.php");
    private DownloadTask usersDownloadTask;
    private JSONObject jsonObjectResult;
    SearchJSONParser searchJSONParser = new SearchJSONParser();
    String stringSearch;
    private String[] userNames;
    private SearchView searchView;
    private String suggestion;
    private LocationManager locationManager;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    //private ImageLoader mImageLoader;
    NavigationView mDrawer;
    ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (NavigationView) findViewById(R.id.navigation_view);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        TabLayout slidingTabLayout = (TabLayout) findViewById(R.id.materialTabHost);
        // Center the tabs in the layout
        // slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainActivityViewPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        viewPager.setOffscreenPageLimit(3);
        slidingTabLayout.setupWithViewPager(viewPager);
        buildGoogleApiClient();
        createLocationRequest();


        buildFAB();


        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();


                //noinspection SimplifiableIfStatement
                if (id == R.id.home) {
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();

                } else if (id == R.id.profile) {
                    Intent intent = new Intent(MainActivity.this, MyProfile.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(false);
                } else if (id == R.id.setting) {
                    Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(false);
                } else if (id == R.id.Myfriends) {
                    Intent intent = new Intent(MainActivity.this, MyFriends.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(false);
                } else if (id == R.id.logOut) {
                    SharedPreferences sharedPref = getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", "notfound");
                    editor.putString("password", "notfound");
                    editor.commit();
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();


                }
                return true;
            }
        });
    }

    public void displayPromptForEnablingGPS(final Activity activity) {
        service = (LocationManager) getSystemService(LOCATION_SERVICE);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            builder.setMessage(message)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    activity.startActivity(new Intent(action));
                                    d.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.cancel();
                                }
                            });
            builder.create().show();
        }
    }


    private String getAutoCompleteUrl(String s) {

        String url = USERS_SEARCH;
        return url;
    }

    private void buildFAB() {
        //define the icon for the main floating action button
        ImageView iconActionButton = new ImageView(this);
        iconActionButton.setImageResource(R.drawable.ic_action_new);

        //set the appropriate background for the main floating action button along with its icon
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(iconActionButton)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(post);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                suggestion = getSuggestion(position);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("userName", suggestion);
                startActivity(intent);
                // Toast.makeText(getApplicationContext(), suggestion, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stringSearch = newText.toString();
                usersDownloadTask = new DownloadTask();
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                String url = getAutoCompleteUrl(newText.toString());
                usersDownloadTask.execute(url);
                return false;
            }
        });
        return true;
    }

    private String getSuggestion(int position) {
        MatrixCursor mCursor = (MatrixCursor) searchView.getSuggestionsAdapter().getItem(
                position);
        String suggest1 = mCursor.getString(mCursor
                .getColumnIndex("userName"));
        return suggest1;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(intent);
        } else if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_search_post) {
            startActivity(new Intent(MainActivity.this, PostSearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationPref();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation

                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationPref();
                return;
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        displayPromptForEnablingGPS(this);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
            }
        });
    }

    private void updateLocationPref() {
        if (mCurrentLocation != null) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("mCurrentLocation", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("Longitude", Double.doubleToLongBits(mCurrentLocation.getLongitude()));
            editor.putLong("Latitude", Double.doubleToLongBits(mCurrentLocation.getLatitude()));
            editor.commit();
            //Toast.makeText(getApplicationContext(), "Current Location has been saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
                //setButtonsEnabledState();
            }
        });
    }

    private class DownloadTask extends AsyncTask<String, Void, MatrixCursor> {
        String error;

        @Override
        protected MatrixCursor doInBackground(String... url) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("target", stringSearch));
            jsonObjectResult = jsonParser.makeHttpRequest(USERS_SEARCH, pairs);
            if (jsonObjectResult == null) {
                error = "Error in the connection";
            }

            try {
                if (jsonObjectResult.getInt("success") == 1) {
                    return searchJSONParser.parse(jsonObjectResult);
                } else
                    error = jsonObjectResult.getString("message");

            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(MatrixCursor result) {
            super.onPostExecute(result);
            String[] from = new String[]{"firstName", "userName"};
            int[] to = new int[]{android.R.id.text1, android.R.id.text2};
            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), android.R.layout.simple_list_item_2, result, from, to);
            searchView.setSuggestionsAdapter(adapter);
        }
    }

    private class AddPlaceTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String error;
        String userName;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String username = sharedPref.getString("username", "default value");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", userName));
            pairs.add(new BasicNameValuePair("longitude", mCurrentLocation.getLongitude() + ""));
            pairs.add(new BasicNameValuePair("latitude", (mCurrentLocation.getLatitude()) + ""));
            jsonObjectResult = jsonParser.makeHttpRequest(ADD_PLACE_URL, pairs);
            if (jsonObjectResult == null) {
                error = "Error int the connection";
                return false;
            }

            try {
                if (jsonObjectResult.getInt("success") == 1)
                    return true;
                else
                    error = jsonObjectResult.getString("message");

            } catch (Exception ex) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), "your location has been added successfully", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), "error your location hasn't been added", Toast.LENGTH_LONG).show();
        }
    }
}