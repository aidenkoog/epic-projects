package io.github.aidenkoog.testdriver.message.v1.payload;

public class Latitude extends PayloadEntity {

    private static final String TAG = Latitude.class.getSimpleName();

    private static final int LENGTH = 12;

    public Latitude() {
        setType(PAYLOAD_TYPE_LATITUDE);
    }

    public Latitude(String latitude) {
        setType(PAYLOAD_TYPE_LATITUDE);
        setValue(stringToByteArrayWithCount(latitude, LENGTH));
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
