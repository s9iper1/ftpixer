package com.byteshaft.ftpixer;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetUpDetails extends AppCompatActivity {
    private RadioButton mWifi;
    private RadioButton mMobileData;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0453A2")));
        setContentView(R.layout.setup);
        RadioGroup dataUsageGroup = (RadioGroup) findViewById(R.id.data_uage_radiogroup);
        mWifi = (RadioButton) findViewById(R.id.wifi_radio_button);
        mMobileData = (RadioButton) findViewById(R.id.mobile_data_radio_button);
    }

    private boolean isWifiConnected() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

    }

    private boolean isMobileDataConnected() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

    }
}