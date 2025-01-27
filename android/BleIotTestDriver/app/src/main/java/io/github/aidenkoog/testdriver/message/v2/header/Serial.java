package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Serial extends HeaderEntity {

    private static final String TAG = Serial.class.getSimpleName();

    public static final int LENGTH = 15;

    public Serial() {
        //  Get IMEI. now hardcoded
        setValue(stringToByteArray("100000000000033"));
    }

    public Serial(byte[] serial) {
        setValue(serial);
    }

    public Serial(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return new String(getValue());
    }
}
