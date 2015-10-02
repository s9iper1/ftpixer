package com.byteshaft.ftpixer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class SetUpDetails extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private RadioButton mWifi;
    private RadioButton mMobileData;
    private RadioGroup dataUsageGroup;
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
        dataUsageGroup = (RadioGroup) findViewById(R.id.data_uage_radiogroup);
        mWifi = (RadioButton) findViewById(R.id.wifi_radio_button);
        mMobileData = (RadioButton) findViewById(R.id.mobile_data_radio_button);
        mContinueButton.setOnClickListener(this);
        mChangeBackgroundButton.setOnClickListener(this);
        mSaveServerSetting.setOnCheckedChangeListener(this);

        if (AppGlobals.getSettingState()) {
            mSaveServerSetting.setChecked(true);
            mServerName.setText(AppGlobals.getServer());
            mPort.setText(AppGlobals.getPort());
            mUsername.setText(AppGlobals.getUsername());
            mPassword.setText(AppGlobals.getPassword());
            String network = AppGlobals.getPreferenceManager().getString(AppGlobals.NETWORK_PREF, null);
            if ("wifi".equals(network)) {
                mWifi.setChecked(true);
                mMobileData.setChecked(false);
            } else if ("data".equals(network)) {
                mMobileData.setChecked(true);
                mWifi.setChecked(false);
            }
        } else {
            mSaveServerSetting.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_continue:
                if ((mServerName.getText().toString().trim()).isEmpty() ||
                        mPort.getText().toString().trim().isEmpty() ||
                        mPassword.getText().toString().trim().isEmpty() ||
                        mUsername.getText().toString().trim().isEmpty() ||
                        dataUsageGroup.getCheckedRadioButtonId() == -1)  {
                    Toast.makeText(getApplicationContext(), "All fields must be filled and selected",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (mSaveServerSetting.isChecked()) {
                    preferences.edit().putString(AppGlobals.SERVER, mServerName.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.PORT, mPort.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.USERNAME, mUsername.getText().toString().trim()).apply();
                    preferences.edit().putString(AppGlobals.PASSWORD, mPassword.getText().toString().trim()).apply();
                    preferences.edit().putString(
                            AppGlobals.NETWORK_PREF,
                            mWifi.isChecked() ? "wifi" : "data").apply();
                    preferences.edit().putBoolean("setting_saved", true).apply();
                } else {
                    AppGlobals.sServerIP = mServerName.getText().toString();
                    AppGlobals.sPortNumber = mPort.getText().toString();
                    AppGlobals.sUsername = mUsername.getText().toString();
                    AppGlobals.sPassword = mPassword.getText().toString();
                    AppGlobals.sNetworkMedium = mWifi.isChecked() ? "wifi" : "data";

                    preferences.edit().putString(AppGlobals.SERVER, null).apply();
                    preferences.edit().putString(AppGlobals.PORT, null).apply();
                    preferences.edit().putString(AppGlobals.USERNAME, null).apply();
                    preferences.edit().putString(AppGlobals.PASSWORD, null).apply();
                    preferences.edit().putString(AppGlobals.NETWORK_PREF, null).apply();
                    preferences.edit().putBoolean("setting_saved", false).apply();
                }
                Intent intent = new Intent(SetUpDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
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
                   if (resultCode == RESULT_OK && null != data) {
                       Uri selectedImage = data.getData();
                       new BitmapTask().execute(selectedImage);
                   } else {
                       Toast.makeText(getApplicationContext(), "You haven't picked an Image",
                               Toast.LENGTH_SHORT).show();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
        }
    }

    class BitmapTask extends AsyncTask<Uri, Void, Void> {

        @Override
        protected Void doInBackground(Uri... params) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), params[0]);
                FileOutputStream out = new FileOutputStream(AppGlobals.getSplashPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}