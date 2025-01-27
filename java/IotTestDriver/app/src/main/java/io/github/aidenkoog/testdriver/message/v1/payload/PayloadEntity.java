package io.github.aidenkoog.testdriver.message.v1.payload;

import android.util.Log;

import io.github.aidenkoog.testdriver.message.exception.*;
import io.github.aidenkoog.testdriver.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PayloadEntity {

    private static final String TAG = PayloadEntity.class.getSimpleName();

    private static final byte TYPE_BYTE_LEN         = 0x01;
    private static final byte LENGTH_BYTE_LEN       = 0x02;

    public static final byte PAYLOAD_TYPE_INDEX                 = 0x01;
    public static final byte PAYLOAD_TYPE_BATLEVEL              = 0x02;
    public static final byte PAYLOAD_TYPE_BEACON_MAC            = 0x03;
    public static final byte PAYLOAD_TYPE_LATITUDE              = 0x04;
    public static final byte PAYLOAD_TYPE_LONGITUDE             = 0x05;
    public static final byte PAYLOAD_TYPE_DATE                  = 0x06;
    public static final byte PAYLOAD_TYPE_SERVER_STATUS         = 0x07;
    public static final byte PAYLOAD_TYPE_CELL_INFORMATION      = 0x08;
    public static final byte PAYLOAD_TYPE_HDOP                  = 0x09;
    public static final byte PAYLOAD_TYPE_EVT_START_TIME        = 0x0A;
    public static final byte PAYLOAD_TYPE_EVT_SENT_TIME         = 0x0B;
    public static final byte PAYLOAD_TYPE_TAKE_RECEIVE_TIME     = 0x0C;
    public static final byte PAYLOAD_TYPE_RESOLVE_RECEIVE_TIME  = 0x0D;
    public static final byte PAYLOAD_FIRMWARE_UPDATE_STATUS     = 0x0E;
    public static final byte PAYLOAD_FIRMWARE_UPDATE_VERSION    = 0x0F;
    public static final byte PAYLOAD_FIRMWARE_TOTAL_SIZE        = 0x10;
    public static final byte PAYLOAD_FIRMWARE_OFFSET            = 0x11;
    public static final byte PAYLOAD_FIRMWARE_LENGTH            = 0x12;
    public static final byte PAYLOAD_FIRMWARE_DATA              = 0x13;

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
            case PAYLOAD_TYPE_INDEX:
                typeString = "Index";
                break;
            case PAYLOAD_TYPE_BATLEVEL:
                typeString = "Batlevel";
                break;
            case PAYLOAD_TYPE_BEACON_MAC:
                typeString = "BeaconMac";
                break;
            case PAYLOAD_TYPE_LATITUDE:
                typeString = "Latitude";
                break;
            case PAYLOAD_TYPE_LONGITUDE:
                typeString = "Longitude";
                break;
            case PAYLOAD_TYPE_DATE:
                typeString = "Date";
                break;
            case PAYLOAD_TYPE_SERVER_STATUS:
                typeString = "ServerStatus";
                break;
            case PAYLOAD_TYPE_CELL_INFORMATION:
                typeString = "CellInformation";
                break;
            case PAYLOAD_TYPE_HDOP:
                typeString = "Hdop";
                break;
            case PAYLOAD_TYPE_EVT_START_TIME:
                typeString = "EvtStartTime";
                break;
            case PAYLOAD_TYPE_EVT_SENT_TIME:
                typeString = "EvtSentTime";
                break;
            case PAYLOAD_TYPE_TAKE_RECEIVE_TIME:
                typeString = "TakeReceiveTime";
                break;
            case PAYLOAD_TYPE_RESOLVE_RECEIVE_TIME:
                typeString = "ResolveReceiveTime";
                break;
            case PAYLOAD_FIRMWARE_UPDATE_STATUS:
                typeString = "FirmwareUpdateStatus";
                break;
            case PAYLOAD_FIRMWARE_UPDATE_VERSION:
                typeString = "FirmwareUpdateVersion";
                break;
            case PAYLOAD_FIRMWARE_TOTAL_SIZE:
                typeString = "FirmwareTotalSize";
                break;
            case PAYLOAD_FIRMWARE_OFFSET:
                typeString = "FirmwareOffset";
                break;
            case PAYLOAD_FIRMWARE_LENGTH:
                typeString = "FirmwareLength";
                break;
            case PAYLOAD_FIRMWARE_DATA:
                typeString = "FirmwareData";
                break;
            default:
                //  Ignore
                break;
        }
        return typeString;
    }

    public static PayloadEntity getPayload(byte type, byte[] value) {
        PayloadEntity entity = null;

        switch(type) {
            case PAYLOAD_TYPE_DATE:
                entity = new Date(value);
                break;
            case PAYLOAD_TYPE_SERVER_STATUS:
                entity = new ServerStatus(value);
                break;
            case PAYLOAD_FIRMWARE_UPDATE_STATUS:
                entity = new FirmwareUpdateStatus(value);
                break;
            case PAYLOAD_FIRMWARE_UPDATE_VERSION:
                entity = new FirmwareUpdateVersion(value);
                break;
            case PAYLOAD_FIRMWARE_TOTAL_SIZE:
                entity = new FirmwareTotalSize(value);
                break;
            case PAYLOAD_FIRMWARE_OFFSET:
                entity = new FirmwareOffset(value);
                break;
            case PAYLOAD_FIRMWARE_LENGTH:
                entity = new FirmwareLength(value);
                break;
            case PAYLOAD_FIRMWARE_DATA:
                entity = new FirmwareData(value);
                break;
            default:
                //  Ignore
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

    public static void parsePayload(List<Byte> byteList,
                List<PayloadEntity> payloads) throws MessageInvalidException {
        byte[] buf = null;

        try {
            while (byteList.isEmpty() == false) {
                byte type = byteList.remove(0);
                short length = (short)(byteList.remove(0) << 8 | byteList.remove(0) << 0);
                buf = new byte[length];
                for (int i = 0; i < buf.length; i++) {
                    buf[i] = byteList.remove(0);
                }
                payloads.add(getPayload(type, buf));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new MessageInvalidException("Payload parsing error!");
        }
    }
}
