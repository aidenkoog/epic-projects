package io.github.aidenkoog.testdriver.message.v1.payload;

public class Index extends PayloadEntity {

    private static final String TAG = Index.class.getSimpleName();

    public Index() {
        setType(PAYLOAD_TYPE_INDEX);
    }

    public Index(byte index) {
        this();
        setValue(new byte[] { index });
    }

    public String toString() {
        return super.toString(String.valueOf((getValue()[0] << 0) & 0xFF));
    }
}
