package io.github.aidenkoog.testdriver.message.v1.payload;

public class Longitude extends PayloadEntity {

    private static final String TAG = Longitude.class.getSimpleName();

    private static final int LENGTH = 12;

    public Longitude() {
        setType(PAYLOAD_TYPE_LONGITUDE);
    }

    public Longitude(String longitude) {
        setType(PAYLOAD_TYPE_LONGITUDE);
        setValue(stringToByteArrayWithCount(longitude, LENGTH));
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
