package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareData extends PayloadEntity {

    private static final String TAG = FirmwareData.class.getSimpleName();

    public FirmwareData() {
        setType(PAYLOAD_FIRMWARE_DATA);
    }

    public FirmwareData(byte[] value) {
        this();
        setValue(value);
    }
}
