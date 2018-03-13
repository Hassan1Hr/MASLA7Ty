package com.hassan.masla7ty.pojo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;


import com.parse.Parse;

import java.lang.reflect.Method;

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
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
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
