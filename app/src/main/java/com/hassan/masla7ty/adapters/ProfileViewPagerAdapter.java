package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 5/7/2015.
 */
public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private List<Fragment> mFragments;
    private List<String> mFragmentTitles;
    String[] tabTitles = {"Posts","Friends","About"};
    private Context context;

    public ProfileViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
    }
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }




    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);

        }




    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position

        return mFragmentTitles.get(position);





    }
}
