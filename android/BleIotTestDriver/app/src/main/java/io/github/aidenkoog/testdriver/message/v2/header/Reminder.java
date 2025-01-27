package io.github.aidenkoog.testdriver.message.v2.header;

import java.nio.ByteBuffer;

public class Reminder extends HeaderEntity {

    private static final String TAG = Reminder.class.getSimpleName();

    public static final int LENGTH = 4;

    public Reminder() {
        setValue(new byte[] { (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0 });
    }

    public Reminder(ByteBuffer buff) {
        value = new byte[LENGTH];
        buff.get(value);
    }

    public Reminder(byte[] value) {
        setValue(value);
    }
}
