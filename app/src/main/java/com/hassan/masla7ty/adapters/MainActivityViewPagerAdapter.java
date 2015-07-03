package com.hassan.masla7ty.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.hassan.masla7ty.R;
import com.hassan.masla7ty.fragments.FriendFragment;
import com.hassan.masla7ty.fragments.PostFragment;
import com.hassan.masla7ty.fragments.ServiceFragment;

/**
 * Created by Hassan on 5/7/2015.
 */
public class MainActivityViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    int icons[] = { R.drawable.ic_action_articles, R.drawable.ic_action_personal,R.drawable.ic_action_home};



    String[] tabTitles = {"Posts","Friends","Services"};
    private Context context;

    public MainActivityViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }




    @Override
    public Fragment getItem(int position) {
        Fragment pagFragment;

        switch (position)
        {
            case 0:pagFragment= PostFragment.newInstance();
                break;
            case 1:pagFragment= FriendFragment.newInstance();
                break;
            case 2:pagFragment= ServiceFragment.newInstance();
                break;


            default:pagFragment = PostFragment.newInstance();
                break;


        }

        return pagFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable drawable = context.getResources().getDrawable(icons[position]);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan image = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        SpannableString span = new SpannableString(" "+tabTitles[position]);
        span.setSpan(image,0,1,span.SPAN_EXCLUSIVE_EXCLUSIVE);
        // span.setSpan(tabTitles[position],span.length()/2,span.length(),span.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }
}
