package io.github.aidenkoog.testdriver.message.v2.header;

import io.github.aidenkoog.testdriver.utils.Utils;

public abstract class HeaderEntity {

    private static final String TAG = HeaderEntity.class.getSimpleName();

    protected static final int HEADER_MSG_VERSION = 0x03;

    protected byte[] value;

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public int getLength() {
        return value.length;
    }

    public String toString() {
        return Utils.byteArrayToHex(getValue());
    }

    protected long byteArrayToNumeral(byte[] src) {
        long value = 0;
        for (int i = 0; i < src.length; i++) {
            value = (value << 8) | (src[i] & 0xFF);
        }
        return value;
    }

    protected byte[] intToByteArray(int src) {
        return new byte[] {(byte)(src >> 8), (byte)src};
    }

    protected byte[] longToByteArray(long src) {
        return new byte[] {(byte)(src >> 24), (byte)(src >> 16), (byte)(src >> 8), (byte)src};
    }

    protected byte[] stringToByteArray(String str) {
        return str.getBytes();
    }

    protected byte[] stringToByteArrayWithCount(String str, int count) {
        byte[] b = new byte[count];

        for (int i = 0; i < str.length(); i++) {
            b[i] = (byte) str.charAt(i);
        }

        return b;
    }
}
