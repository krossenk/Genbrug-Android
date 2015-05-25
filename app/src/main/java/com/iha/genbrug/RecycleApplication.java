package com.iha.genbrug;

import android.app.Application;

/**
 * Created by Morten on 25-05-2015.
 */
import android.app.Application;
import android.content.Context;

import java.util.List;

public class RecycleApplication extends Application {
    private static RecycleApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static RecycleApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
