package com.byteshaft.ftpixer;


import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Helpers {

    public static String getDataDirectory() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String directoryPath = dataDirectory
                + AppGlobals.getContext().getPackageName()
                + File.separator;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static int getPreviousCounterValue() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                AppGlobals.getContext());
        return sharedPreferences.getInt(AppGlobals.COUNTER_VALUE, 1);
    }

    public static void saveCounterValue(int counterValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                AppGlobals.getContext());
        sharedPreferences.edit().putInt(AppGlobals.COUNTER_VALUE, counterValue).apply();
    }

    public static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
}
