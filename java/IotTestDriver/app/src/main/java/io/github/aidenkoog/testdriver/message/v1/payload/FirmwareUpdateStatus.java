package io.github.aidenkoog.testdriver.message.v1.payload;

public class FirmwareUpdateStatus extends PayloadEntity {

    private static final String TAG = FirmwareUpdateStatus.class.getSimpleName();

    public FirmwareUpdateStatus() {
        setType(PAYLOAD_FIRMWARE_UPDATE_STATUS);
    }

    public FirmwareUpdateStatus(byte[] value) {
        this();
        setValue(value);
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
