package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Ret extends HeaderEntity {

    private static final String TAG = Ret.class.getSimpleName();

    public static final byte REQUEST            = 0x01;
    public static final byte RESPONSE           = 0x02;
    public static final byte ERROR              = 0x03;

    public static final int LENGTH = 1;

    public Ret(byte value) {
        setValue(new byte[] { value });
    }

    public Ret(byte[] value) {
        setValue(value);
    }

    public Ret(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public short get() {
        return (short) byteArrayToNumeral(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
