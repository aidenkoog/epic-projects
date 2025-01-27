package io.github.aidenkoog.testdriver.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String currentTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getDateFromTimeMillis(long timeMillis) {
        return (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")).format(new Date(timeMillis));
    }

    public static String byteArrayToHex(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 2);

        for(byte b: byteArray) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static String byteListToHex(List<Byte> byteList) {
        StringBuilder sb = new StringBuilder(byteList.size() * 2);

        for(Byte b: byteList) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static List<Byte> byteArrayToArrayList(byte[] byteArray) {
        List<Byte> list = new ArrayList<Byte>();

        for(byte b : byteArray) {
            list.add(b);
        }

        return list;
    }

    public static byte[] ArrayListTobyteArray(List<Byte> byteList) {
        int i = 0;
        byte[] bytes = new byte[byteList.size()];

        for(Byte b : byteList) {
            bytes[i++] = b.byteValue();
        }

        return bytes;
    }

    public static long byteArrayToNumeral(byte[] src) {
        long value = 0;
        for (int i = 0; i < src.length; i++) {
            value = (value << 8) | (src[i] & 0xFF);
        }
        return value;
    }
}
