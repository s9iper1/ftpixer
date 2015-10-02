package com.byteshaft.ftpixer;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

public class AppGlobals  extends Application {
    private static SharedPreferences sPreferences;
    public static String sServerIP;
    public static String sPortNumber;
    public static String sUsername;
    public static String sPassword;
    public static String sNetworkMedium;
    public static String sWorkingDir;
    public static final String SERVER = "server";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    public static final String NETWORK_PREF = "network_preference";
    public static final String WORKING_DIR = "working_dir";
    public static final String COUNTER_VALUE = "counter_value";
    public static Context sContext;
    public static final String LOGTAG = "ftpixer";
    private static ConnectivityManager connectivityManager;
    private static File currentPath;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        connectivityManager = (ConnectivityManager) sContext.getSystemService(CONNECTIVITY_SERVICE);
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

    public static String getUsername() {
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

    public static boolean isWifiConnected() {
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

    }

    public static boolean isMobileDataConnected() {
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

    }

    public static boolean isInternetConnected() {
        return isWifiConnected() || isMobileDataConnected();
    }

    public static String getInternetPreference() {
        return sPreferences.getString(AppGlobals.NETWORK_PREF, null);
    }

    public static File getSplashPath() {
        return new File(Environment.getExternalStorageDirectory()
                + File.separator + "splash.jpg");
    }

    public static void setCurrentPath(File file) {
        currentPath = file;
    }

    public static File getCurrentPath() {
        return currentPath;
    }
}
