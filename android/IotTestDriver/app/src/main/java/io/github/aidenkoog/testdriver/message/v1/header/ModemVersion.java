package io.github.aidenkoog.testdriver.message.v1.header;

public class ModemVersion extends HeaderEntity {

    private static final String TAG = ModemVersion.class.getSimpleName();

    private static final int LENGTH = 22;

    public ModemVersion() {
        //  Get Modem Version. now hardcoded
        setValue(stringToByteArrayWithCount("MC60CAR01A08", LENGTH));
    }

    public String toString() {
        return (new String(getValue())).trim();
    }
}
