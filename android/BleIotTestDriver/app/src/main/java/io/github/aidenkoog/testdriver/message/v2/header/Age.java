package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Age extends HeaderEntity {

    private static final String TAG = Age.class.getSimpleName();

    public static final int LENGTH = 1;

    public Age() {
        setValue(new byte[] { 0 });
    }

    public Age(byte[] value) {
        setValue(value);
    }

    public Age(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
