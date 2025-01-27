package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class FirmwareVersion extends HeaderEntity {

    private static final String TAG = FirmwareVersion.class.getSimpleName();

    public static final int LENGTH = 8;

    public FirmwareVersion() {
        //  Get IMEI. now hardcoded
        setValue(stringToByteArrayWithCount("0.0.2.37", LENGTH));
    }

    public FirmwareVersion(byte[] firmwareVersion) {
        setValue(firmwareVersion);
    }

    public FirmwareVersion(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public String toString() {
        return (new String(getValue())).trim();
    }
}
