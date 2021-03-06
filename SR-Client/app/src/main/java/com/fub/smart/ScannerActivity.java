package com.fub.smart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private int scanGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scanGoal = getIntent().getExtras().getInt("scanGoal");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

                Toast.makeText(ScannerActivity.this, "Permission is granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions();
            }
        }


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]) {

        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    final boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted) {
                        Toast.makeText(ScannerActivity.this, "Permission Granted!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ScannerActivity.this, "Permission Denied!", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                displayAlertMessage("you need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void displayAlertMessage(String msg, DialogInterface.OnClickListener listner) {
        new AlertDialog.Builder(ScannerActivity.this).setPositiveButton("Ok", listner)
                .setNegativeButton("Cancel", null).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {

        String scanResult = result.getText();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scannerView.resumeCameraPreview(ScannerActivity.this);
                    }
                })
                .setMessage(scanResult).create().show();*/
        if (scanGoal == 1) {
            Intent myIntent = new Intent(getBaseContext(), DefineProduct.class);
            myIntent.putExtra("scanResult",scanResult);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), AddItemActivity.class);
            myIntent.putExtra("scanResult",scanResult);
            startActivity(myIntent);
        }


    }
}
