package com.byteshaft.ftpixer;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppGlobals  extends Application {
    private static SharedPreferences sPreferences;
    public static String sServerIP;
    public static String sPortNumber;
    public static String sUsername;
    public static String sPassword;
    public static String sWorkingDir;
    public static final String SERVER = "server";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    public static final String WORKING_DIR = "working_dir";
    public static final String COUNTER_VALUE = "counter_value";
    public static Context sContext;
    public static final String LOGTAG = "ftpixer";

    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sContext = getApplicationContext();
    }

    public static final String getLogTag(Class aClass) {
        return LOGTAG+ aClass.getName();
    }



    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }

    public static String getUSername() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(USERNAME, "");
    }

    public static String getPassword() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(PASSWORD, "");
    }

    public static String getServer() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(SERVER, "");
    }

    public static String getPort() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(PORT, "");
    }

    public static boolean getSettingState() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean("setting_saved", false);
    }
}
