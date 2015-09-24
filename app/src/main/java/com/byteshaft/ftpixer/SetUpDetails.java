package com.byteshaft.ftpixer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SetUpDetails extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RadioButton mWifi;
    private RadioButton mMobileData;
    private ConnectivityManager connectivityManager;
    private EditText mServerName;
    private EditText mPort;
    private EditText mUsername;
    private EditText mPassword;
    private Button mContinueButton;
    private Button mChangeBackgroundButton;
    private CheckBox mSaveServerSetting;
    private SharedPreferences preferences;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0453A2")));
        setContentView(R.layout.setup);
        setTitle("Settings");
        preferences = AppGlobals.getPreferenceManager();
        mSaveServerSetting = (CheckBox) findViewById(R.id.save_settings);
        mContinueButton = (Button) findViewById(R.id.button_continue);
        mChangeBackgroundButton = (Button) findViewById(R.id.change_splash_screen);
        mServerName = (EditText) findViewById(R.id.server_name);
        mPort = (EditText) findViewById(R.id.port);
        mUsername = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        RadioGroup dataUsageGroup = (RadioGroup) findViewById(R.id.data_uage_radiogroup);
        mWifi = (RadioButton) findViewById(R.id.wifi_radio_button);
        mMobileData = (RadioButton) findViewById(R.id.mobile_data_radio_button);
        mContinueButton.setOnClickListener(this);
        mChangeBackgroundButton.setOnClickListener(this);
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
            case R.id.change_splash_screen:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
        }
}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.save_settings:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
               try {
                   if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                       SharedPreferences preferences = AppGlobals.getPreferenceManager();
                       preferences.edit().putString("splash_bg", data.getData().toString()).apply();
                   } else {
                       Toast.makeText(getApplicationContext(), "You haven't picked Image",
                               Toast.LENGTH_SHORT).show();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
        }
    }
}