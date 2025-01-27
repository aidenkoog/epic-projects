package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Weight extends HeaderEntity {

    private static final String TAG = Weight.class.getSimpleName();

    public static final int LENGTH = 2;

    public Weight() {
        setValue(new byte[] { (byte)0x00, (byte)0x00 });
    }

    public Weight(byte[] value) {
        setValue(value);
    }

    public Weight(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
