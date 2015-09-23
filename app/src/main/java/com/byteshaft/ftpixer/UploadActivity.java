package com.byteshaft.ftpixer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadActivity extends Activity implements View.OnClickListener {

    Button startAgain, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        startAgain = (Button) findViewById(R.id.start_again);
        exitButton = (Button) findViewById(R.id.exit_button);
        startAgain.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        upLoadDialog();
    }

    private void upLoadDialog() {
        ProgressDialog progressDialog = new ProgressDialog(UploadActivity.this);
        progressDialog.setTitle("Uploading To FTP");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_again:
                Intent intent = new Intent(this, SplashScreen.class);
                startActivity(intent);
                finish();
                MainActivity.mainActivity.finish();
                break;
            case R.id.exit_button:
                finish();
                moveTaskToBack(true);
                MainActivity.mainActivity.finish();
        }
    }
}
