package io.github.aidenkoog.testdriver.message.v1.header;

import java.util.Random;

public class MessageId extends HeaderEntity {

    private static final String TAG = MessageId.class.getSimpleName();

    public static final int LENGTH = 4;

    public static byte[] genMessageId() {
        byte[] msgId = new byte[LENGTH];

        Random rand = new Random();
        rand.nextBytes(msgId);

        return msgId;
    }

    public MessageId() {
        setValue(genMessageId());
    }

    public MessageId(byte[] value) {
        setValue(value);
    }

    public String toString() {
        return String.valueOf(byteArrayToNumeral(getValue()));
    }
}
