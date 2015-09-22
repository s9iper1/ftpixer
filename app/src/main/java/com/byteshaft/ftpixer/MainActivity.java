package com.byteshaft.ftpixer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mScanButton;
    private Button mPicButton;
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    ScannerActivity scannerActivity;
    public static EditText scannerEditText;
    static RadioButton jobNumberRadioButton;
    static RadioButton regNoRadioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scannerActivity = new ScannerActivity();
        scannerEditText = (EditText) findViewById(R.id.barCodeEditText);
        scannerEditText.clearFocus();
        jobNumberRadioButton = (RadioButton) findViewById(R.id.job_no_radio_button);
        regNoRadioButton = (RadioButton) findViewById(R.id.reg_no_radio_button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mScanButton = (Button) findViewById(R.id.scan_button);
        mPicButton  = (Button) findViewById(R.id.pic_button);
        imageView = (ImageView) findViewById(R.id.imageView1);
        mScanButton.setOnClickListener(this);
        mPicButton.setOnClickListener(this);
        scannerEditText.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(6)});
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (jobNumberRadioButton.isChecked()) {
                    scannerEditText.setText("");
                    int maxLength = 6;
                       scannerEditText.setFilters(new InputFilter[]
                                {new InputFilter.LengthFilter(maxLength)});
                    System.out.println("Job Number Button Checked");
                } else if (regNoRadioButton.isChecked()) {
                    scannerEditText.setText("");
                    int maxLength = 7;
                    scannerEditText.setFilters(new InputFilter[]
                            {new InputFilter.LengthFilter(maxLength)});
                    System.out.println("Registration Button Checked");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_button:
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.pic_button:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            System.out.println(finalFile);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}