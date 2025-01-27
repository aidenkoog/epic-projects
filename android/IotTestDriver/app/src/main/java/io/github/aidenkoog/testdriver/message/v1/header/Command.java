package io.github.aidenkoog.testdriver.message.v1.header;

public class Command extends HeaderEntity {

    private static final String TAG = Command.class.getSimpleName();

    public static final byte SOS                = 0x01;
    public static final byte FALL               = 0x02;
    public static final byte STRAP_ON           = 0x03;
    public static final byte STRAP_OFF          = 0x04;
    public static final byte POWER_ON           = 0x05;
    public static final byte POWER_OFF          = 0x06;
    public static final byte LOW_BATTERY        = 0x07;
    public static final byte VERY_LOW_BATTERY   = 0x08;
    public static final byte CHARGING           = 0x09;
    public static final byte RECONNECTED        = 0x0A;
    public static final byte PACKET_TEST        = 0x0B;
    public static final byte CANCEL             = 0x0C;

    public static final byte PRIORITY_NORMAL            = 0x01;
    public static final byte PRIORITY_EMERGENCY         = 0x02;
 
    public static final byte ACTION_TAKECARE    = 0x10;
    public static final byte ACTION_RESOLVE     = 0x20;
    public static final byte ACTION_CANCEL      = 0x30;

    public static final byte FORMAT_OTHERS_RSQ      = (byte)0x40;
    public static final byte FORMAT_OTHERS_RSP      = (byte)0x80;
    public static final byte FORMAT_OTHERS_ERROR    = (byte)0xC0;

    public static final byte ALERT_BIT              = 0x0F;
    public static final byte ACTION_BIT             = 0x30;
    public static final byte FORMAT_BIT             = (byte)0xC0;

    public static class Event {
        private byte type;
        private byte action;
        private byte format;
        private byte description;
        private boolean priority = false;

        public Event(byte type) {
            this.type = type;
            this.priority = getPriority(type);
            this.action = 0;
            this.format = FORMAT_OTHERS_RSQ;
        }

        public byte getType() {
            return type;
        }

        public byte getAction() {
            return action;
        }

        public byte getFormat() {
            return format;
        }

        public byte getByte() {
            return (byte)(type|action|format);
        }

        public boolean isCancel() {
            return (action & ACTION_CANCEL) == ACTION_CANCEL;
        }

        public void setCancel() {
            action = ACTION_CANCEL;
            priority = false;
        }

        public boolean getPriority() {
            return priority;
        }

        public void setPriority(boolean priority) {
            this.priority = priority;
        }

        public boolean isPriority() {
            return priority;
        }

        public String getDescription() {
            return getDescription(type);
        }

        public String toString() {
            return "Need to be added";
        }

        public static String getCommandDesc(final byte command) {
            return getDescription((byte)(command & ALERT_BIT)) + "+"
                + getActionDesc((byte)(command & ACTION_BIT)) + "+"
                + getFormatDesc((byte)(command & FORMAT_BIT));
        }

        private static boolean getPriority(byte type) {
            switch (type) {
                case SOS:
                case FALL:
                    return true;

                default:
                    return false;
            }
        }

        public static String getDescription(final byte type) {
            switch (type) {
                case SOS:
                    return "SOS";
                case FALL:
                    return "FALL";
                case STRAP_ON:
                    return "STRAP_ON";
                case STRAP_OFF:
                    return "STRAP_OFF";
                case POWER_ON:
                    return "POWER_ON";
                case POWER_OFF:
                    return "POWER_OFF";
                case LOW_BATTERY:
                    return "LOW_BATTERY";
                case VERY_LOW_BATTERY:
                    return "VERY_LOW_BATTERY";
                case CHARGING:
                    return "CHARGING";
                case RECONNECTED:
                    return "RECONNECTED";
                case PACKET_TEST:
                    return "PACKET_TEST";
                case CANCEL:
                    return "CANCEL";
                default:
                    return "";
            }
        }

        public static String getActionDesc(final byte action) {
            switch (action) {
                case ACTION_TAKECARE:
                    return "TC";
                case ACTION_RESOLVE:
                    return "RV";
                case ACTION_CANCEL:
                    return "CC";
                default:
                    return "NO";
            }
        }

        public static String getFormatDesc(final byte format) {
            switch (format) {
                case FORMAT_OTHERS_RSQ:
                    return "RQ";
                case FORMAT_OTHERS_RSP:
                    return "RS";
                default:
                    return "ER";
            }
        }
    }

    public Command() {
        setValue( (byte)0 );
    }

    public Command(byte b) {
        setValue(new byte[] { (byte)b });
    }

    public void setValue(byte value) {
        setValue(new byte[] { (byte)value });
    }

    private byte getCommand() {
        return (byte)(getValue()[0]);
    }

    public boolean isAction() {
        return (getCommand() & ACTION_BIT) != 0;
    }

    public boolean isRequest() {
        if ((getCommand() & FORMAT_OTHERS_ERROR) == FORMAT_OTHERS_ERROR) {
            return false;
        }
        return (getCommand() & FORMAT_OTHERS_RSQ) == FORMAT_OTHERS_RSQ;
    }

    public boolean isResponse() {
        if ((getCommand() & FORMAT_OTHERS_ERROR) == FORMAT_OTHERS_ERROR) {
            return false;
        }
        return (getCommand() & FORMAT_OTHERS_RSP) == FORMAT_OTHERS_RSP;
    }

    public boolean isCancel() {
        return (getCommand() & ACTION_BIT) == ACTION_CANCEL;
    }

    public boolean isTake() {
        return (getCommand() & ACTION_BIT) == ACTION_TAKECARE;
    }

    public boolean isResolve() {
        return (getCommand() & ACTION_BIT) == ACTION_RESOLVE;
    }


    public String toString() {
        return Event.getCommandDesc(getValue()[0]);
    }
}
