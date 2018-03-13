package com.hassan.masla7ty.map;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Hassan on 6/16/2015.
 */
public class UserLocation extends Fragment{
    public static Location mLocation;
    public static Context mContext;
    static UserLocation userLocation;
    public UserLocation()
    {
        mContext =(Context) getActivity();
    }
    public static UserLocation getInctance()
    {
        if (userLocation == null) {
            userLocation = new UserLocation();

            return userLocation;
        } else
            return userLocation;
    }



}
