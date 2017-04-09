package com.byteshaft.ftpixer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.byteshaft.ftp4j.FTPAbortedException;
import com.byteshaft.ftp4j.FTPClient;
import com.byteshaft.ftp4j.FTPDataTransferException;
import com.byteshaft.ftp4j.FTPDataTransferListener;
import com.byteshaft.ftp4j.FTPException;
import com.byteshaft.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;



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
                uploadFile(AppGlobals.sServerIP, AppGlobals.sUsername,
                        Integer.parseInt(AppGlobals.sPortNumber), AppGlobals.sPassword);
            } else if (AppGlobals.getSettingState() && !(AppGlobals.getUsername().trim()).isEmpty() &&
                    !(AppGlobals.getPassword().trim()).isEmpty() &&
                    !(AppGlobals.getServer().trim()).isEmpty() &&
                    !(AppGlobals.getPort().trim()).isEmpty()) {

                uploadFile(AppGlobals.getServer(), AppGlobals.getUsername(),
                        Integer.parseInt(AppGlobals.getPort()),
                        AppGlobals.getPassword());
            }
            return null;
        }
    }

    public void uploadFile(String host, String username, int port, String password) {
        File folderPath = new File(Environment.getExternalStorageDirectory(),
                File.separator + "Android/data" + File.separator + getPackageName() + File.separator);
        File[] folderCount = folderPath.listFiles();
        for (File folder : folderCount) {
            for (File files : folder.listFiles()) {
                arrayList.add(files.getAbsolutePath());
            }
        }
        imagesCount = arrayList.size();
        addPerUpdate = imagesCount / 100;
        currentProgress = 0;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setType(FTPClient.TYPE_BINARY);
            File[] allFolders = folderPath.listFiles();
            for (File folder : allFolders) {
                ftpClient.changeDirectory("/");
                try {
                    ftpClient.changeDirectory(folder.getName());
                } catch (FTPException ignore) {
                    ftpClient.createDirectory(folder.getName());
                    ftpClient.changeDirectory(folder.getName());
                }
                for (File files: folder.listFiles()) {
                    ftpClient.upload(files, new MyTransferListener());
                    progressDialog.setProgress(progressDialog.getProgress() + addPerUpdate);
                }
                removeFiles(folderPath+ File.separator + folder.getName());

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

    void removeFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
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