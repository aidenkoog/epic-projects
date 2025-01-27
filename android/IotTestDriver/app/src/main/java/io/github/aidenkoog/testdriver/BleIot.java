package io.github.aidenkoog.testdriver;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

public class BleIoT extends MultiDexApplication {

    private static final String TAG = BleIoT.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG ? true : false;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
