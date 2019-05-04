package com.example.tvd.bmdandpfcal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BottomNavigationBehavior extends Activity {
    public static final int RequestPermissionCode = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissionsMandAbove();
                Intent intent = new Intent(BottomNavigationBehavior.this,LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
    @TargetApi(23)
    public void checkPermissionsMandAbove() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {
            if (checkPermission()) {
                requestPermission();
            } else {
                requestPermission();
            }
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(BottomNavigationBehavior.this, new String[]
                {
                        READ_PHONE_STATE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        WRITE_CONTACTS
                }, RequestPermissionCode);
    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadPhoneStatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadLocationPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (ReadPhoneStatePermission && ReadStoragePermission && ReadLocationPermission) {

                    } else {
                        Toast.makeText(BottomNavigationBehavior.this, "Required All Permissions to granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

}
