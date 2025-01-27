package io.github.aidenkoog.testdriver.message.v2.payload;

public class EmgStatus extends PayloadEntity {

    private static final String TAG = EmgStatus.class.getSimpleName();

    public static final byte CANCEL             = 0x01;
    public static final byte RESOLVE            = 0x02;
    public static final byte TAKEN              = 0x03;

    public EmgStatus() {
        setType(PAYLOAD_TYPE_SERVER_EMG_STATUS);
    }

    public EmgStatus(byte type) {
        this();
        setValue(new byte[] { type });
    }

    public EmgStatus(byte[] bytes) {
        this();
        setValue(bytes);
    }

    public short get() {
        return (short) byteArrayToNumeral(value);
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
