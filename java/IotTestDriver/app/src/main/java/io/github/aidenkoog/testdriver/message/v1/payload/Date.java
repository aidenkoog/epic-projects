package io.github.aidenkoog.testdriver.message.v1.payload;

import android.content.Context;
import android.os.BatteryManager;

import io.github.aidenkoog.testdriver.BleIoT;

public class Date extends PayloadEntity {

    private static final String TAG = Date.class.getSimpleName();

    public Date() {
        setType(PAYLOAD_TYPE_DATE);
    }

    public Date(byte[] value) {
        this();
        this.setValue(value);
    }

    public String getDate() {
        return new String(value);
    }

    public String toString() {
        return super.toString(getDate());
    }
}
