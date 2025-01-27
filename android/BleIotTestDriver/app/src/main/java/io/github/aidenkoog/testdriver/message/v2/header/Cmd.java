package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Cmd extends HeaderEntity {

    private static final String TAG = Cmd.class.getSimpleName();

    public static final int LENGTH = 1;

    public Cmd(byte value) {
        setValue(new byte[] { value });
    }

    public Cmd(byte[] value) {
        setValue(value);
    }

    public Cmd(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
