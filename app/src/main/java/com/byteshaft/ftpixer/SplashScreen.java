package com.byteshaft.ftpixer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    static boolean isExitButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Button continueButton = (Button)findViewById(R.id.continue_button);
        Button setup = (Button)findViewById(R.id.set_up);
        continueButton.setOnClickListener(this);
        setup.setOnClickListener(this);

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