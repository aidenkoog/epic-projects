package io.github.aidenkoog.testdriver.message;

import static io.github.aidenkoog.testdriver.message.Notification.CANCEL;

import android.util.Log;

public abstract class Part {

    private static final String TAG = Part.class.getSimpleName();

    public static final byte PART_NOTIFICATION      = 0x01;
    public static final byte PART_STATUS            = 0x02;

    protected byte part;
    protected byte type;

    public void setPart(byte part) {
        this.part = part;
    }

    public byte getPart() {
        return part;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setCancel() {
        if (this instanceof Notification) {
            if (this.isPriority() == true) {
               this.setType(CANCEL);
            } else {
                Log.e(TAG, "Not Priority!");
            }
        } else {
            Log.e(TAG, "Not Supported!");
        }
    }

    public boolean isPriority() {
        if (this instanceof Notification) {
            switch (type) {
                case Notification.SOS:
                case Notification.FALL:
                    return true;

                default:
                    return false;
            }
        }
        return false;
    }

    public static String getPartString(byte part) {
        String typeString = null;
        switch(part) {
            case PART_NOTIFICATION:
                typeString = "NOTIFICATION";
                break;
            case PART_STATUS:
                typeString = "STATUS";
                break;
            default:
                typeString = "Not defined";
                break;
        }
        return typeString;
    }

    public String toString() {
        return "[" + getPartString(part) + ":" + type + "]";
    }

    public String toString(String str) {
        return "[" + getPartString(part) + ":" + str + "]";
    }
}
