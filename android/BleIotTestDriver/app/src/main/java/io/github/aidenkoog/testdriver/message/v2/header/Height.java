package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Height extends HeaderEntity {

    private static final String TAG = Height.class.getSimpleName();

    public static final int LENGTH = 2;

    public Height() {
        setValue(new byte[] { (byte) 0x00, (byte) 0x00 });
    }

    public Height(byte[] value) {
        setValue(value);
    }

    public Height(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
