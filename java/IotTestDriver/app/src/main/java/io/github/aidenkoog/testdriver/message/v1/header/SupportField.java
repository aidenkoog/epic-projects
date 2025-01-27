package io.github.aidenkoog.testdriver.message.v1.header;

public class SupportField extends HeaderEntity {

    private static final String TAG = SupportField.class.getSimpleName();

    private static final byte MSG_SUPPORT_FALL               = (byte)0x01;
    private static final byte MSG_SUPPORT_STRAP_ON           = (byte)0x02;
    private static final byte MSG_SUPPORT_STRAP_OFF          = (byte)0x04;
    private static final byte MSG_SUPPORT_POWER_ON           = (byte)0x08;
    private static final byte MSG_SUPPORT_POWER_OFF          = (byte)0x10;
    private static final byte MSG_SUPPORT_LOW_BATTERY        = (byte)0x20;
    private static final byte MSG_SUPPORT_VERY_LOW_BATTERY   = (byte)0x40;
    private static final byte MSG_SUPPORT_CHARGING           = (byte)0x80;

    public SupportField() {
        //  Get Modem Version. now hardcoded
        setValue(new byte[] { (byte)(MSG_SUPPORT_FALL
                    | MSG_SUPPORT_STRAP_ON
                    | MSG_SUPPORT_STRAP_OFF
                    | MSG_SUPPORT_POWER_ON
                    | MSG_SUPPORT_POWER_OFF
                    | MSG_SUPPORT_LOW_BATTERY
                    | MSG_SUPPORT_VERY_LOW_BATTERY
                    | MSG_SUPPORT_CHARGING) });
    }

    public SupportField(byte value) {
        setValue(new byte[] { value });
    }

    public void setValue(byte value) {
        setValue(new byte[] { value });
    }
}
