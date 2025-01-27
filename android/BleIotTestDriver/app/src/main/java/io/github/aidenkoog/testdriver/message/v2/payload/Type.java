package io.github.aidenkoog.testdriver.message.v2.payload;

public class Type extends PayloadEntity {

    private static final String TAG = Type.class.getSimpleName();

    public Type() {
        setType(PAYLOAD_TYPE_TYPE);
    }

    public Type(byte type) {
        this();
        setValue(new byte[] { type });
    }

    public Type(byte[] bytes) {
        this();
        setValue(bytes);
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
