package io.github.aidenkoog.testdriver.system;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Pogo implements Comparable {

    private static final String TAG = Pogo.class.getSimpleName();

    private static final int MSG_POGO_EXPIRED = 0x0001;

    private static final long POGO_EXPIRED_TIME = TimeUnit.SECONDS.toMillis(3);

    private String mAddress;

    private int mRssi;

    private boolean mExpired;

    private Handler pogoExpiredTimer;

    private void initHandler() {
        pogoExpiredTimer = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_POGO_EXPIRED:
                        mExpired = true;
                        Log.e(TAG, "pogoExpiredTimer handler:" + mAddress);
                        break;
                }
            }
        };
    }

    public Pogo(String address, int rssi, boolean expired) {
        initHandler();

        mAddress = address;
        mRssi = rssi;
        mExpired = expired;
    }

    public Pogo(String address, int rssi) {
        this(address, rssi, false);
    }

    public String getAddress() {
        return mAddress;
    }

    public String getAddressWithoutColon() {
        return mAddress.replace(":", "");
    }

    public void setAddress(String address) {
        mAddress = address;
        mExpired = false;
        updated();
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssi) {
        mRssi = rssi;
        mExpired = false;
        updated();
    }

    public void added() {
        refreshPogoExpiredTimer();
    }

    public void updated() {
        refreshPogoExpiredTimer();
    }

    public void removed() {
        cancelPogoExpiredTimer();
    }

    public boolean isExpired() {
        return mExpired;
    }

    @Override
    public int compareTo(Object object) {
        Pogo pogo = ((Pogo)object);
        if (this.getRssi() < pogo.getRssi()) {
            return 1;
        } else if (this.getRssi() > pogo.getRssi()) {
            return -1;
        }
        return 0;
    }

    private void refreshPogoExpiredTimer() {
        if (pogoExpiredTimer != null) {
            pogoExpiredTimer.removeCallbacksAndMessages(null);
            pogoExpiredTimer.sendEmptyMessageDelayed(MSG_POGO_EXPIRED, POGO_EXPIRED_TIME);
        }
    }

    private void cancelPogoExpiredTimer() {
        if (pogoExpiredTimer != null) {
            pogoExpiredTimer.removeCallbacksAndMessages(null);
        }
    }

    public String toString() {
        return "Address(" + mAddress + "), RSSI(" + mRssi + ")";
    }
}
