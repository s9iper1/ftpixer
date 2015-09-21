package com.byteshaft.ftpixer;


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
        setContentView(R.layout.setup);


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mWifi = (RadioButton) findViewById(R.id.wifi_button);
        mMobileData = (RadioButton) findViewById(R.id.mobile_data_button);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mWifi.isChecked() && isWifiConnected()) {
                    System.out.println("wifi");

                } else if (mMobileData.isChecked() && isMobileDataConnected()) {

                    System.out.println("data");
                }
            }
        });
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