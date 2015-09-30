package com.byteshaft.ftpixer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class UploadActivity extends Activity implements View.OnClickListener {

    private Button startAgain;
    private Button exitButton;
    private ProgressDialog progressDialog;
    private ArrayList<String> arrayList;
    private int imagesCount;
    private int addPerUpdate;
    private int currentProgress;

    int fileSize = 0;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        arrayList = intent.getStringArrayListExtra("images");
        imagesCount = arrayList.size();
        addPerUpdate = imagesCount / 100;
        currentProgress = 0;
        startAgain = (Button) findViewById(R.id.start_again);
        exitButton = (Button) findViewById(R.id.exit_button);
        startAgain.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        new UploadTask().execute(arrayList);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(progressBroadCastReceiver);
    }

    private void upLoadDialog() {
        progressDialog = new ProgressDialog(UploadActivity.this);
        progressDialog.setTitle("Uploading To FTP");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(currentProgress);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_again:
                Intent intent = new Intent(this, MainActivity.class);
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

    class UploadTask extends AsyncTask<ArrayList<String>, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            upLoadDialog();
        }

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            Log.i(AppGlobals.getLogTag(getClass()), "uploading started");
            if (!AppGlobals.getSettingState()
                    && AppGlobals.sPassword != null
                    && AppGlobals.sPortNumber != null
                    && AppGlobals.sServerIP != null) {
                uploadFile(AppGlobals.sServerIP, AppGlobals.sUsername, Integer.parseInt(AppGlobals.sPortNumber), AppGlobals.sPassword, arrayList);
            } else if (AppGlobals.getSettingState() && !(AppGlobals.getUSername().trim()).isEmpty() &&
                    !(AppGlobals.getPassword().trim()).isEmpty() &&
                    !(AppGlobals.getServer().trim()).isEmpty() &&
                    !(AppGlobals.getPort().trim()).isEmpty()) {

                uploadFile(AppGlobals.getServer(), AppGlobals.getUSername(),
                        Integer.parseInt(AppGlobals.getPort()),
                        AppGlobals.getPassword(), arrayLists[0]);
            }
            return null;
        }
    }



    public void uploadFile(String host, String username, int port, String password,
                           ArrayList<String> files) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setType(FTPClient.TYPE_BINARY);
            for (String image : files) {
                File file = new File(image);
                ftpClient.upload(file, new MyTransferListener());
                progressDialog.setProgress(progressDialog.getProgress() + addPerUpdate);

            }
        } catch (UnknownHostException ignore) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(
                            UploadActivity.this,
                            "Cannot resolve host address, is internet working ?",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            });
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            e.printStackTrace();
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {
            progressDialog.setProgress(0);
        }

        public void transferred(int length) {
            progressDialog.setProgress(counter * 2);
            counter++;
        }

        public void completed() {
            progressDialog.setProgress(100);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            getApplicationContext(),
                            "Upload Completed ...",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void aborted() {
        }

        public void failed() {
        }

    }
}