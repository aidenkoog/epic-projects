package io.github.aidenkoog.testdriver.message.v2.payload;

public class Location extends PayloadEntity {

    private static final String TAG = Location.class.getSimpleName();

    private static final int LENGTH = 30;

    public Location() {
        setType(PAYLOAD_TYPE_LOCATION);
    }

    public Location(String location) {
        setType(PAYLOAD_TYPE_LOCATION);
        setValue(stringToByteArrayWithCount(location, LENGTH));
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
