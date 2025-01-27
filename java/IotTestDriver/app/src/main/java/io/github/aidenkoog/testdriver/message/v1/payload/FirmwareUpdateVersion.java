package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareUpdateVersion extends PayloadEntity {

    private static final String TAG = FirmwareUpdateVersion.class.getSimpleName();

    public FirmwareUpdateVersion() {
        setType(PAYLOAD_FIRMWARE_UPDATE_VERSION);
    }

    public FirmwareUpdateVersion(byte[] value) {
        this();
        setValue(value);
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
