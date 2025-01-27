package io.github.aidenkoog.testdriver.message.v1.payload;

import android.content.Context;
import android.os.BatteryManager;

import io.github.aidenkoog.testdriver.BleIoT;

public class Battery extends PayloadEntity {

    private static final String TAG = Battery.class.getSimpleName();

    public Battery() {
        setType(PAYLOAD_TYPE_BATLEVEL);
        setValue(new byte[] { (byte)getBatteryLevel() });
    }

    private byte getBatteryLevel() {
        BatteryManager bm = (BatteryManager) BleIoT.getContext().getSystemService(Context.BATTERY_SERVICE);
        assert bm != null;
        return (byte)bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
