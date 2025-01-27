package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareTotalSize extends PayloadEntity {

    private static final String TAG = FirmwareTotalSize.class.getSimpleName();

    public static final int LENGTH = 4;

    public FirmwareTotalSize() {
        setType(PAYLOAD_FIRMWARE_TOTAL_SIZE);
    }

    public FirmwareTotalSize(byte[] value) {
        this();
        setValue(value);
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
