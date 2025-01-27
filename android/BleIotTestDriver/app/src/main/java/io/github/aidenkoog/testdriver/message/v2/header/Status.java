package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Status extends HeaderEntity {

    private static final String TAG = Status.class.getSimpleName();

    public static final int EMERGENCY_STATUS_SHIFT = 32;
    public static final int EMERGENCY_STATUS_MASK = 0B111111;

    public static final int LENGTH = 8;

    public Status() {
        setValue(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00} );
    }

    public Status(byte[] value) {
        setValue(value);
    }

    public Status(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        //return Long.toBinaryString(byteArrayToNumeral(getValue()));
        return String.format("%s %s %s %s %s %s %s %s",
                bytesToBinaryString(value[0]),
                bytesToBinaryString(value[1]),
                bytesToBinaryString(value[2]),
                bytesToBinaryString(value[3]),
                bytesToBinaryString(value[4]),
                bytesToBinaryString(value[5]),
                bytesToBinaryString(value[6]),
                bytesToBinaryString(value[7]));

    }

    private String bytesToBinaryString(byte b) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(((0x80 >>> i) & b) == 0 ? '0' : '1');
        }

        return builder.toString();
    }
}
