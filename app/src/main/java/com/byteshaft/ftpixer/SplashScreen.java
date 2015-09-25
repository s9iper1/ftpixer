package com.byteshaft.ftpixer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLDecoder;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    static boolean isExitButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ImageView splash = (ImageView) findViewById(R.id.imageView);
        String splashBg = AppGlobals.getSplashPath().getAbsolutePath();
        splash.setImageURI(Uri.parse(splashBg));
        Button continueButton = (Button)findViewById(R.id.continue_button);
        Button setup = (Button)findViewById(R.id.set_up);
        continueButton.setOnClickListener(this);
        setup.setOnClickListener(this);
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
                Intent intent = new Intent(this, SetUpDetails.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                isExitButtonPressed = false;
                break;
        }
    }
}