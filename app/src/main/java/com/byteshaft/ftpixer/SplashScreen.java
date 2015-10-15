package com.byteshaft.ftpixer;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    static boolean isExitButtonPressed = false;
    Dialog dialog2;
    String storedPassword;
    SharedPreferences sharedPreferences;
    Button okPasswordButton;
    EditText passwordEditText;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        storedPassword = sharedPreferences.getString("password_one", null);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ImageView splash = (ImageView) findViewById(R.id.imageView);
        String splashBg = AppGlobals.getSplashPath().getAbsolutePath();
        if (new File(splashBg).exists()) {
            splash.setImageURI(Uri.parse(splashBg));
        } else {
            splash.setBackgroundResource(R.drawable.splash);
        }
        Button continueButton = (Button)findViewById(R.id.continue_button);
        Button setup = (Button)findViewById(R.id.set_up);
        continueButton.setOnClickListener(this);
        setup.setOnClickListener(this);
    }


    public void openAskPasswordDialog() {
        dialog2 = new Dialog(SplashScreen.this);
        dialog2.setContentView(R.layout.custom_layout2);
        dialog2.show();
        okPasswordButton = (Button) dialog2.findViewById(R.id.button_ok_password_entry);
        passwordEditText = (EditText) dialog2.findViewById(R.id.ask_password_edit_text);
        okPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String enteredPassword = passwordEditText.getText().toString().trim();
                if (enteredPassword.trim().equals(storedPassword.trim())) {
                    Intent mainActivity = new Intent(SplashScreen.this, SetUpDetails.class);
                    startActivity(mainActivity);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else {
                    passwordEditText.getText().clear();
                    passwordEditText.setError("Password does not match");
                    vibrator.vibrate(500);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_button:
                if (!AppGlobals.getSettingState()) {
                    Toast.makeText(getApplicationContext(),
                            "Cannot proceed, you have to setup first", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
            case R.id.set_up:
                if (storedPassword == null) {
                    Intent intent = new Intent(this, SetUpDetails.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }else {
                    openAskPasswordDialog();
                }
                isExitButtonPressed = false;
                break;
        }
    }
}