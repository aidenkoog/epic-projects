package io.github.aidenkoog.testdriver.message.v2.payload;

import android.util.Log;

import io.github.aidenkoog.testdriver.message.exception.MessageInvalidException;

import java.nio.ByteBuffer;
import java.util.List;

public abstract class PayloadEntity {

    private static final String TAG = PayloadEntity.class.getSimpleName();

    private static final byte TYPE_BYTE_LEN                     = 0x01;
    private static final byte LENGTH_BYTE_LEN                   = 0x02;

    public static final byte PAYLOAD_TYPE_TYPE                  = 0x01;
    public static final byte PAYLOAD_TYPE_INDEX                 = 0x02;
    public static final byte PAYLOAD_TYPE_BATLEVEL              = 0x03;
    public static final byte PAYLOAD_TYPE_LOCATION              = 0x05;
    public static final byte PAYLOAD_TYPE_CELL_INFORMATION      = 0x06;
    public static final byte PAYLOAD_TYPE_WIFI_INFORMATION      = 0x0B;

    public static final byte PAYLOAD_TYPE_RSP_TYPE              = 0x10;
    public static final byte PAYLOAD_TYPE_SERVER_EMG_STATUS     = 0x20;

    protected byte type;
    protected byte[] value;

    public void setType(byte type) {
        this.type = type;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte getType() {
        return type;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] getBytes() {

        if (value == null) {
            return null;
        }

        byte[] b = new byte[getLength()];

        b[0] = type;
        b[1] = (byte)((value.length & 0xFF00) >> 8);
        b[2] = (byte)((value.length & 0x00FF) >> 0);

        System.arraycopy(value, 0, b, 3, value.length);

        return b;
    }

    public int getLength() {

        if (value == null) {
            return 0;
        }

        return TYPE_BYTE_LEN + LENGTH_BYTE_LEN + value.length;
    }

    public String toString() {
        return "[" + getTypeString(type) + ":" + value + "]";
    }

    public String toString(String str) {
        return "[" + getTypeString(type) + ":" + str + "]";
    }

    public static String getTypeString(byte type) {
        String typeString = null;
        switch(type) {
            case PAYLOAD_TYPE_TYPE:
                typeString = "Type";
                break;
            case PAYLOAD_TYPE_INDEX:
                typeString = "Index";
                break;
            case PAYLOAD_TYPE_BATLEVEL:
                typeString = "Batlevel";
                break;
            case PAYLOAD_TYPE_LOCATION:
                typeString = "Location";
                break;
            case PAYLOAD_TYPE_SERVER_EMG_STATUS:
                typeString = "ServerEmgStatus";
                break;
            default:
                typeString = "Not defined";
                break;
        }
        return typeString;
    }

    public static PayloadEntity getPayload(byte type, byte[] value) {
        PayloadEntity entity = null;

        switch(type) {
            case PAYLOAD_TYPE_RSP_TYPE:
                entity = new Type(value);
                break;
            case PAYLOAD_TYPE_SERVER_EMG_STATUS:
                entity = new EmgStatus(value);
                break;
            default:
                Log.e(TAG, "Not defined Payload!");
                break;
        }
        return entity;
    }

    protected long byteArrayToNumeral(byte[] src) {
        long value = 0;
        for (int i = 0; i < src.length; i++) {
            value = (value << 8) | (src[i] & 0xFF);
        }
        return value;
    }

    protected byte[] intToByteArray(int src) {
        return new byte[] {(byte)(src >> 8), (byte)src};
    }

    protected byte[] stringToByteArray(String str) {
        return str.getBytes();
    }

    protected byte[] stringToByteArrayWithCount(String str, int count) {
        byte[] b = new byte[Math.min(str.length(), count)];

        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) str.charAt(i);
        }

        return b;
    }

    public static void parsePayload(ByteBuffer buff,
                                    List<PayloadEntity> payloads) throws MessageInvalidException {
        byte[] bytes = null;

        try {
            while (buff.remaining() > 0) {
                byte type = buff.get();
                short length = buff.getShort();
                bytes = new byte[length];
                buff.get(bytes);
                payloads.add(getPayload(type, bytes));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new MessageInvalidException("Payload parsing error!");
        }
    }
}
