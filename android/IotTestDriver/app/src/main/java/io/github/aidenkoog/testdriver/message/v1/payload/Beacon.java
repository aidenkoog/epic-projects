package io.github.aidenkoog.testdriver.message.v1.payload;

public class Beacon extends PayloadEntity {

    private static final String TAG = Beacon.class.getSimpleName();

    private static final int LENGTH = 12;

    public Beacon() {
        setType(PAYLOAD_TYPE_BEACON_MAC);
    }

    public Beacon(String address) {
        setType(PAYLOAD_TYPE_BEACON_MAC);
        setValue(stringToByteArrayWithCount(address, LENGTH));
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
