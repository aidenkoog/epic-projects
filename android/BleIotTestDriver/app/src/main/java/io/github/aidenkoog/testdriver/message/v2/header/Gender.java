package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Gender extends HeaderEntity {

    private static final String TAG = Gender.class.getSimpleName();

    public static final int LENGTH = 1;

    public Gender() {
        setValue(new byte[] { (byte) 0x00 });
    }

    public Gender(byte[] value) {
        setValue(value);
    }

    public Gender(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
