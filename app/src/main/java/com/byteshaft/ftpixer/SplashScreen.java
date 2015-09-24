package com.byteshaft.ftpixer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    static boolean isExitButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ImageView splash = (ImageView) findViewById(R.id.imageView);
        String splashBg = getSPlash();
        if (splashBg != null) {
            splash.setImageURI(Uri.parse(splashBg));
        }

        Button continueButton = (Button)findViewById(R.id.continue_button);
        Button setup = (Button)findViewById(R.id.set_up);
        continueButton.setOnClickListener(this);
        setup.setOnClickListener(this);

    }

    private String getSPlash() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getString("splash_bg", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_button:
                Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
            case R.id.set_up:
                System.out.println("set");
                Intent intent = new Intent(this, SetUpDetails.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                isExitButtonPressed = false;
                break;
        }
    }
}