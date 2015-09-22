package com.byteshaft.ftpixer.uploadtask;


import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.connectors.FTPProxyConnector;

public class FtpUpload extends IntentService {

    private static final String FTP_HOST = "192.168.1.2";
    private static final String FTP_USER = "";
    private static final String FTP_PORT = "22";

    public FtpUpload() {
        super("ftpUpload");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void uploadFile (File file) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST, 22);
            ftpClient.login(FTP_USER, "");
            ftpClient.setType(FTPClient.TYPE_BINARY);
            ftpClient.changeDirectory("");
            ftpClient.upload(file , new MyTransferListener());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {
            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
        }

        public void transferred(int length) {
            Toast.makeText(getBaseContext(), " transferred ..." + length, Toast.LENGTH_SHORT).show();
        }

        public void completed() {
            Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
        }

        public void aborted() {
            Toast.makeText(getApplicationContext()," transfer aborted ,", Toast.LENGTH_SHORT).show();
        }

        public void failed() {
            System.out.println(" failed ..." );
        }

    }

}
