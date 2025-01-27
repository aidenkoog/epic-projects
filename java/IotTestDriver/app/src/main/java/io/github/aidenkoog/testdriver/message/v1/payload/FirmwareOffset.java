package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareOffset extends PayloadEntity {

    private static final String TAG = FirmwareOffset.class.getSimpleName();

    public FirmwareOffset() {
        setType(PAYLOAD_FIRMWARE_OFFSET);
    }

    public FirmwareOffset(byte[] value) {
        this();
        setValue(value);
    }
}
