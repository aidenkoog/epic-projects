package io.github.aidenkoog.testdriver.message.v1.payload;

import io.github.aidenkoog.testdriver.utils.Utils;

public class EventStartTime extends PayloadEntity {

    private static final String TAG = EventStartTime.class.getSimpleName();

    private static final int LENGTH = 14;

    public EventStartTime() {
        setType(PAYLOAD_TYPE_EVT_START_TIME);
    }

    public EventStartTime(String value) {
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
