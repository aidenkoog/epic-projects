package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class SourceIp extends HeaderEntity {

    private static final String TAG = SourceIp.class.getSimpleName();

    public static final int LENGTH = 4;

    private long mSourceIp = 0;

    public SourceIp() {
        setValue(longToByteArray(mSourceIp));
    }

    public SourceIp(byte[] value) {
        setValue(value);
    }

    public SourceIp(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
