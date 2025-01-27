package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;
import java.time.Instant;

public class Date extends HeaderEntity {

    private static final String TAG = Date.class.getSimpleName();

    public static final int LENGTH = 4;

    public Date() {
        setValue(longToByteArray(Instant.now().getEpochSecond()));
    }

    public Date(byte[] value) {
        setValue(value);
    }

    public Date(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
