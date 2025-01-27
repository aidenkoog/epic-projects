package io.github.aidenkoog.testdriver.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import io.github.aidenkoog.testdriver.BleIot;

public class PositionManager {

    private static final String TAG = PositionManager.class.getSimpleName();

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 second

    private Location mLocation;
    private LocationManager mLocationManager;

    private boolean mIsPositioning = false;
    private boolean mIsPositionReady = false;
    private OnPositionManagerListener mListener;

    private PositionManager() {
        mLocationManager = (LocationManager) BleIot.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private static class SingletonData {
        private static final PositionManager instance = new PositionManager();
    }

    public static PositionManager getInstance() {
        return SingletonData.instance;
    }

    public interface OnPositionManagerListener {
        void onPositionReady();
    }

    public boolean isPositionReady() {
        return mIsPositionReady;
    }

    public void startPositioning() {
        if (mLocationManager == null || mIsPositioning == true) {
            return;
        }
        mIsPositioning = true;
        mLocation = null;

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                MIN_TIME_BW_UPDATES,
                                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                                mLocationListener,
                                                Looper.getMainLooper());
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                MIN_TIME_BW_UPDATES,
                                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                                mLocationListener,
                                                Looper.getMainLooper());


        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            mLocation = location;
        }
        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            mLocation = location;
        }

        mIsPositionReady = false;
    }

    public void startPositioning(final OnPositionManagerListener listener) {
        mListener = listener;

        startPositioning();
    }
    public void stopPositioning() {
        mIsPositioning = false;
        if (mLocationManager == null) {
            return;
        }
        mLocationManager.removeUpdates(mLocationListener);

        mLocation = null;
        mIsPositionReady = false;
    }

    public double getLatitude() {
        if (mLocation != null) {
            return mLocation.getLatitude();
        }
        return 0;
    }

    public double getLongitude() {
        if (mLocation != null) {
            return mLocation.getLongitude();
        }
        return 0;
    }

    public float getAccuracy() {
        if (mLocation != null) {
            return mLocation.getAccuracy();
        }
        return 0;
    }

    private LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged, location:" + location);
            if (mIsPositioning == false) {
                return;
            }
            if (mIsPositionReady == false) {
                mIsPositionReady = true;
                if (mListener != null) {
                    mListener.onPositionReady();
                }
            }
            mLocation = location;
        }

        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}
