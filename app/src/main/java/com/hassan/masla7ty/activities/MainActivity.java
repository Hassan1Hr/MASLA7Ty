
        package com.hassan.masla7ty.activities;


        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.support.design.widget.NavigationView;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import android.support.v4.view.MenuItemCompat;
        import android.support.v4.view.ViewPager;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.widget.SearchView;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;


        import com.hassan.masla7ty.R;
        import com.hassan.masla7ty.adapters.MainActivityViewPagerAdapter;

        import com.hassan.masla7ty.views.SlidingTabLayout;
        import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;





        public class MainActivity extends ActionBarActivity  {



    private Toolbar toolbar;
    //private ImageLoader mImageLoader;
    NavigationView  mDrawer;
    ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainActivityViewPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.materialTabHost);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        slidingTabLayout.setViewPager(viewPager);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        final ActionBar actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (NavigationView)findViewById(R.id.navigation_view);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


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
                }
                else if(id == R.id.profile) {
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.setting) {
                    Intent intent = new Intent(MainActivity.this, MyProfile.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                else if(id == R.id.Myfriends) {
                    Intent intent = new Intent(MainActivity.this, MyFriends.class);
                    startActivity(intent);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                }





                return true;
            }
        });
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


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
            Toast.makeText(this, "Hey you just hit " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerToggle.onConfigurationChanged(newConfig);
    }


}