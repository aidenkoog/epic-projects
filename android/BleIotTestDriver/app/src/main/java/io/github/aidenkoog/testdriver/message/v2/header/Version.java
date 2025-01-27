package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Version extends HeaderEntity {

    private static final String TAG = Version.class.getSimpleName();

    public Version() {
        setValue(new byte[] { (byte)HEADER_MSG_VERSION });
    }

    public Version(byte value) {
        setValue(new byte[] { value });
    }

    public Version(ByteBuffer buff) {
        setValue(new byte[] { buff.get() });
    }

    public void setValue(byte value) {
        setValue(new byte[] { value });
    }
}
