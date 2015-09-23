package com.byteshaft.ftpixer;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppGlobals  extends Application {
    private static SharedPreferences sPreferences;

    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }
}
