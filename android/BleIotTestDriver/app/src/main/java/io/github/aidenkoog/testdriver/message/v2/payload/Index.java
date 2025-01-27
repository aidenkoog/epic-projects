package io.github.aidenkoog.testdriver.message.v2.payload;

public class Index extends PayloadEntity {

    private static final String TAG = Index.class.getSimpleName();

    public Index() {
        setType(PAYLOAD_TYPE_INDEX);
    }

    public Index(int index) {
        this();
        setValue(intToByteArray(index));
    }

    public String toString() {
        return super.toString(String.valueOf(byteArrayToNumeral(getValue())));
    }
}
