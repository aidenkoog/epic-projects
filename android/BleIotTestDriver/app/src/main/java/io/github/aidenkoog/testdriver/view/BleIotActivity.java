package io.github.aidenkoog.testdriver.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import io.github.aidenkoog.testdriver.R;
import io.github.aidenkoog.testdriver.service.BleIotService;
import io.github.aidenkoog.testdriver.utils.PermissionUtils;

public class BleIotActivity extends Activity implements OnClickListener {

    private static final String TAG = BleIotActivity.class.getSimpleName();

    private static final int COARSE_LOCATION_REQUEST_CODE = 100;

    private static final int ACCESS_BLUETOOTH_REQUEST_CODE = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
    }

    private boolean checkBluetoothPermission() {
        if (PermissionUtils.bluetoothPermission(this)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                ACCESS_BLUETOOTH_REQUEST_CODE);
            return false;
        }
    }

    private boolean checkLocationPermission() {
        if (PermissionUtils.locationCoarsePermission(this)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                COARSE_LOCATION_REQUEST_CODE);
            return false;
        }
    }

    private boolean startBleIotService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this) == false) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                    Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return false;
            }
        }

        if (checkLocationPermission() == false
            || checkBluetoothPermission() == false) {
            return false;
        }

        startService(new Intent(this, BleIotService.class));
        return true;
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();

        switch (view) {
            case R.id.start:
                startBleIotService();
                break;
            case R.id.stop:
                stopService(new Intent(this, BleIotService.class));
                break;
            case R.id.end:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {

        for (int i = 0; i < permissions.length; i++) {
            Log.e(TAG, "requestCode : " + requestCode + " Permission : " + permissions[i] + " grantResult : " + grantResults[i]);
        }

        switch (requestCode) {
            case ACCESS_BLUETOOTH_REQUEST_CODE:
            case COARSE_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    String permission = "";
                    boolean allGranted = true;
                    for (int i = 0; i < permissions.length; i++) {
                        int grant = grantResults[i];
                        allGranted = allGranted && (grant == PackageManager.PERMISSION_GRANTED);
                        if (allGranted == false) {
                            permission = permissions[i].substring(
                                permissions[i].lastIndexOf(".") + 1, permissions[i].length());
                            break;
                        }
                    }

                    if (allGranted == true) {
                        startBleIotService();
                        break;
                    }

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this, permission)) {
                        Toast.makeText(this,
                            permission + " denied. Please allow permission.",
                            Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                            Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                        Toast.makeText(this,
                            permission + " denied. It must be allowed in Settings (app info)",
                            Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}

