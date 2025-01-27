package io.github.aidenkoog.testdriver.message.v1.header;

public class Serial extends HeaderEntity {

    private static final String TAG = Serial.class.getSimpleName();

    public Serial() {
        //  Get IMEI. now hardcoded
        setValue(stringToByteArray("357144090260659"));
        //setValue(stringToByteArray("357144090260660"));
        //setValue(stringToByteArray("861359033600030"));
    }

    public String toString() {
        return new String(getValue());
    }
}
