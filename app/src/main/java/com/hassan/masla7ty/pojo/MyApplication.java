package com.hassan.masla7ty.pojo;

import android.app.Application;
import android.content.Context;

import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;
import com.parse.Parse;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static Context mAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Parse.initialize(this, "ZU0jhKBHWK58OA4A54gsKcyh6wBAN4pvDKb9oZxh", "jNtWphhHRIZRKopJC9Nd9s5hjqGwDFi4zoc8Xp5s");
        new Instabug.Builder(this, "9c44ce333c79d1f24b1b6add5461f608")
                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
                .build();

        this.setAppContext(getApplicationContext());
    }


    public static MyApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
