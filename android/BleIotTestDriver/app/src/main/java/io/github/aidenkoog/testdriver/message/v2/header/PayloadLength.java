package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class PayloadLength extends HeaderEntity {

    private static final String TAG = PayloadLength.class.getSimpleName();

    public static final int LENGTH = 2;

    public PayloadLength() {
        //  Get Modem Version. now hardcoded
        setValue(new byte[] { (byte)((0 & 0xFF00) >> 8), (byte)((0 & 0x00FF) >> 0) });
    }

    public PayloadLength(byte[] value) {
        setValue(value);
    }

    public PayloadLength(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public void setValue(short value) {
        setValue(new byte[] { (byte)((value & 0xFF00) >> 8), (byte)((value & 0x00FF) >> 0) });
    }

    public int get() {
       return (int) byteArrayToNumeral(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
