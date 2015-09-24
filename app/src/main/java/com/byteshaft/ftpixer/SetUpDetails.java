package com.byteshaft.ftpixer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SetUpDetails extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RadioButton mWifi;
    private RadioButton mMobileData;
    private ConnectivityManager connectivityManager;
    private EditText mServerName;
    private EditText mPort;
    private EditText mUsername;
    private EditText mPassword;
    private Button mContinueButton;
    private CheckBox mSaveServerSetting;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0453A2")));
        setContentView(R.layout.setup);
        preferences = AppGlobals.getPreferenceManager();
        mSaveServerSetting = (CheckBox) findViewById(R.id.save_settings);
        mContinueButton = (Button) findViewById(R.id.button_continue);
        mServerName = (EditText) findViewById(R.id.server_name);
        mPort = (EditText) findViewById(R.id.port);
        mUsername = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        RadioGroup dataUsageGroup = (RadioGroup) findViewById(R.id.data_uage_radiogroup);
        mWifi = (RadioButton) findViewById(R.id.wifi_radio_button);
        mMobileData = (RadioButton) findViewById(R.id.mobile_data_radio_button);
        mContinueButton.setOnClickListener(this);
        mSaveServerSetting.setOnCheckedChangeListener(this);
        dataUsageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.wifi_radio_button && isWifiConnected()) {
                    System.out.println("wifi network");
                }

                if (checkedId == R.id.data_uage_radiogroup && isMobileDataConnected()) {
                    System.out.println("mobile network");

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_continue:
                if ((mServerName.getText().toString().trim()).isEmpty() ||
                        (mPort.getText().toString().trim()).isEmpty() ||
                        (mPassword.getText().toString().trim()).isEmpty() ||
                        (mUsername.getText().toString().trim()).isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SetUpDetails.this, MainActivity.class);
                    startActivity(intent);
                }

                if (mSaveServerSetting.isChecked()) {
                    preferences.edit().putString(AppGlobals.SERVER, mServerName.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.PORT, mPort.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.USERNAME, mUsername.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.PASSWORD, mPassword.getText().toString().trim()).apply();
                    preferences.edit().putBoolean("setting_saved", true).apply();
                } else {
                    AppGlobals.sServerIP = mServerName.getText().toString();
                    AppGlobals.sPortNumber = mPort.getText().toString();
                    AppGlobals.sUsername = mUsername.getText().toString();
                    AppGlobals.sPassword = mPassword.getText().toString();
                }
                break;
        }
}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.save_settings:
                break;
        }
    }
}