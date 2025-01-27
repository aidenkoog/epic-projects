package io.github.aidenkoog.testdriver.message.v1.header;

public class Version extends HeaderEntity {

    private static final String TAG = Version.class.getSimpleName();

    public Version() {
        setValue(new byte[] { (byte)HEADER_MSG_VERSION });
    }

    public Version(byte value) {
        setValue(new byte[] { value });
    }

    public void setValue(byte value) {
        setValue(new byte[] { value });
    }
}
