package io.github.aidenkoog.testdriver;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class BleIot extends MultiDexApplication {

    private static final String TAG = BleIot.class.getSimpleName();
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
