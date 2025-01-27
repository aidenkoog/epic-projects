package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Sequence extends HeaderEntity {

    private static final String TAG = Sequence.class.getSimpleName();

    public static final int LENGTH = 2;

    private static int mSequence = 0;

    public Sequence() {
        setValue(intToByteArray(mSequence++));
    }

    public Sequence(byte[] value) {
        setValue(value);
    }

    public Sequence(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
