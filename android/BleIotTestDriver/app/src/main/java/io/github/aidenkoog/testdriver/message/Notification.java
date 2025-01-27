package io.github.aidenkoog.testdriver.message;

public class Notification extends Part {

    private static final String TAG = Notification.class.getSimpleName();

    public static final byte SOS                = 0x01;
    public static final byte FALL               = 0x02;
    public static final byte POWER_ON           = 0x05;
    public static final byte CANCEL             = 0x0C;

    public Notification(byte notification) {
        setPart(PART_NOTIFICATION);
        this.setType(notification);
    }

    public String getDescription() {
        switch (type) {
            case SOS:
                return "SOS";
            case FALL:
                return "FALL";
            case POWER_ON:
                return "POWER_ON";
            default:
                return "Not Defined";
        }
    }

    public String toString() {
        return super.toString(getDescription());
    }
}
