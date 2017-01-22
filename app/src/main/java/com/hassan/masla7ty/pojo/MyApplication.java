package com.hassan.masla7ty.pojo;

import android.app.Application;
import android.content.Context;


import com.parse.Parse;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static Context mAppContext;
    public static final String UsernamePrefernce = "username" ;
    public static final String UserLocationPrefernce = "userLocation";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.setAppContext(getApplicationContext());
        Parse.initialize(this, "ZU0jhKBHWK58OA4A54gsKcyh6wBAN4pvDKb9oZxh", "jNtWphhHRIZRKopJC9Nd9s5hjqGwDFi4zoc8Xp5s");

    }


    public static MyApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context AppContext) {
        this.mAppContext = AppContext;
    }
}
