package io.github.aidenkoog.testdriver.message.v1.payload;

import io.github.aidenkoog.testdriver.utils.Utils;

public class TakeReceiveTime extends PayloadEntity {

    private static final String TAG = TakeReceiveTime.class.getSimpleName();

    private static final int LENGTH = 14;

    public TakeReceiveTime() {
        setType(PAYLOAD_TYPE_TAKE_RECEIVE_TIME);
    }

    public TakeReceiveTime(String value) {
        this();
        this.setValue(value);
    }

    public void setValue(String value) {
        setValue(stringToByteArrayWithCount(value, LENGTH));
    }

    public String toString() {
        return super.toString(Utils.getDateFromTimeMillis(Long.parseLong(new String(getValue()))));
    }
}
