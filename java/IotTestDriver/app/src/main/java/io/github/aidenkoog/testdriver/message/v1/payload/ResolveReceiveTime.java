package io.github.aidenkoog.testdriver.message.v1.payload;

import io.github.aidenkoog.testdriver.utils.Utils;

public class ResolveReceiveTime extends PayloadEntity {

    private static final String TAG = ResolveReceiveTime.class.getSimpleName();

    private static final int LENGTH = 14;

    public ResolveReceiveTime() {
        setType(PAYLOAD_TYPE_RESOLVE_RECEIVE_TIME);
    }

    public ResolveReceiveTime(String value) {
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
