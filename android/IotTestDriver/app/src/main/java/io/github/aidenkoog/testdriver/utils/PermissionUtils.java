package io.github.aidenkoog.testdriver.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    public static boolean locationCoarsePermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean bluetoothPermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
    }
}
