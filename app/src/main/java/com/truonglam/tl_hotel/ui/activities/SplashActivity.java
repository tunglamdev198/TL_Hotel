package com.truonglam.tl_hotel.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.truonglam.tl_hotel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 100;

    @BindView(R.id.spin_kit)
    ProgressBar spinkit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Wave wave = new Wave();
        spinkit.setIndeterminateDrawable(wave);
        checkPermissions();
    }

    private void openHomePageActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openHomePageActivity();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openHomePageActivity();
            return;
        }

        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            openHomePageActivity();
        }
        else {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}


