package io.github.aidenkoog.testdriver.message.v1.payload;

import io.github.aidenkoog.testdriver.utils.Utils;

public class EventSentTime extends PayloadEntity {

    private static final String TAG = EventSentTime.class.getSimpleName();

    private static final int LENGTH = 14;

    public EventSentTime() {
        setType(PAYLOAD_TYPE_EVT_SENT_TIME);
    }

    public EventSentTime(String value) {
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
