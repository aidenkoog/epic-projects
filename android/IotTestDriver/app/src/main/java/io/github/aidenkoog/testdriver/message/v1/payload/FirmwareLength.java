package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareLength extends PayloadEntity {

    private static final String TAG = FirmwareLength.class.getSimpleName();

    public FirmwareLength() {
        setType(PAYLOAD_FIRMWARE_LENGTH);
    }

    public FirmwareLength(byte[] value) {
        this();
        setValue(value);
    }
}
