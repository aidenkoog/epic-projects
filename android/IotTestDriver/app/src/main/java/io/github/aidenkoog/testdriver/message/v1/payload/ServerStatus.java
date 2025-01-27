package io.github.aidenkoog.testdriver.message.v1.payload;

public class ServerStatus extends PayloadEntity {

    private static final String TAG = ServerStatus.class.getSimpleName();

    public static final byte SOS        = 0x01;
    public static final byte FALL       = 0x02;
    public static final byte TAKECARE   = 0x04;
    public static final byte RESOLVE    = 0x08;

    public ServerStatus() {
        setType(PAYLOAD_TYPE_SERVER_STATUS);
    }

    public ServerStatus(byte[] value) {
        this();
        setValue(value);
    }

    public byte getServerStatus() {
        return (byte)(getValue()[0] & 0x0F);
    }

    public boolean isPriority() {
        byte checkBit = SOS | FALL;
        if ((getServerStatus() & (SOS | FALL)) != 0) {
            return true;
        }
        return false;
    }

    private byte getPriority() {
        if ((getServerStatus() & SOS) == SOS) {
            return SOS;
        } else if ((getServerStatus() & FALL) == FALL) {
            return FALL;
        }
        return 0x00;
    }

    public boolean isSos() {
        if (getPriority() == SOS) {
            return true;
        }
        return false;
    }

    public boolean isFall() {
        if (getPriority() == FALL) {
            return true;
        }
        return false;
    }

    private byte getAction() {
        if ((getServerStatus() & RESOLVE) == RESOLVE) {
            return RESOLVE;
        }
        if ((getServerStatus() & TAKECARE) == TAKECARE) {
            return TAKECARE;
        }
        return 0x00;
    }

    public boolean isTake() {
        if ((getServerStatus() & TAKECARE) == TAKECARE) {
            return true;
        }
        return false;
    }

    public boolean isResolve() {
        if ((getServerStatus() & RESOLVE) == RESOLVE) {
            return true;
        }
        return false;
    }

    private String getServerStatusString() {
        StringBuffer serverStatusString = new StringBuffer();
        byte checkBit = SOS | FALL;

        if (isPriority() == true) {
            if ((getServerStatus() & SOS) == SOS) {
                serverStatusString.append("SOS ");
            }
            if ((getServerStatus() & FALL) == FALL) {
                serverStatusString.append("FALL ");
            }
            if ((getServerStatus() & TAKECARE) == TAKECARE) {
                serverStatusString.append("TAKECARE ");
            }
            if ((getServerStatus() & RESOLVE) == RESOLVE) {
                serverStatusString.append("RESOLVE ");
            }
        } else {
            serverStatusString.append("IDLE ");
        }
        return serverStatusString.toString();
    }

    public String toString() {
        return super.toString(getServerStatusString());
    }
}
