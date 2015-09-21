package com.byteshaft.ftpixer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Button continu = (Button)findViewById(R.id.continue_button);
        Button setup = (Button)findViewById(R.id.set_up);
        continu.setOnClickListener(this);
        setup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_button:
                Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(mainActivity);

                break;
            case R.id.set_up:
                System.out.println("set");
                Intent intent = new Intent(this, SetUpDetails.class);
                startActivity(intent);
                break;
        }
    }
}
