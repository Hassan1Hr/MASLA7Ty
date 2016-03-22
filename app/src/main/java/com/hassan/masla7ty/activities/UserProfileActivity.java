package com.hassan.masla7ty.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hassan.masla7ty.R;
import com.hassan.masla7ty.adapters.ProfileViewPagerAdapter;
import com.hassan.masla7ty.fragments.PostFragment;
import com.hassan.masla7ty.fragments.UserFriendsFragement;
import com.hassan.masla7ty.fragments.UserInfoFragment;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private ListView mListView;
    private Fragment postFragment;
    private Fragment friendFragment;
    private Fragment aboutFragment;
    public static String username;





    public static ViewPager viewPager;
    ActionBarDrawerToggle drawerToggle;
    TabLayout tabLayout;
    CoordinatorLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = getIntent().getStringExtra("userName");
        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        if (viewPager != null) {
            initializeFragments();
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout2);
            tabLayout.setupWithViewPager(viewPager);
        }



    }

    private void initializeFragments() {
        postFragment = PostFragment.newInstance();
        friendFragment = UserFriendsFragement.newInstance();
        aboutFragment = UserInfoFragment.newInstance();
    }


    private void setupViewPager(ViewPager viewPager) {
        ProfileViewPagerAdapter mAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(), UserProfileActivity.this);
        mAdapter.addFragment(postFragment, "posts");
        mAdapter.addFragment(friendFragment, "friends");
        mAdapter.addFragment(aboutFragment, "about");
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }





}
