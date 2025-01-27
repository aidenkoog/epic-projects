package io.github.aidenkoog.testdriver.message.v1.header;

public class FirmwareVersion extends HeaderEntity {

    private static final String TAG = FirmwareVersion.class.getSimpleName();

    private static final int LENGTH = 10;

    public FirmwareVersion() {
        //  Get IMEI. now hardcoded
        setValue(stringToByteArrayWithCount("1.0.3.29", LENGTH));
    }

    public String toString() {
        return (new String(getValue())).trim();
    }
}
