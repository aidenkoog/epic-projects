package io.github.aidenkoog.testdriver.message.v2.payload;

import java.nio.ByteBuffer;

public class CellInfo extends PayloadEntity {

    private static final String TAG = CellInfo.class.getSimpleName();

    private static final int LENGTH = 14;

    public CellInfo() {
        setType(PAYLOAD_TYPE_CELL_INFORMATION);
    }

    public CellInfo(int mcc, int mnc, int lac, int cellId, int strength) {
        this();
        setValue(getCellInfoBytes(mcc, mnc, lac, cellId, strength));
    }

    private byte[] getCellInfoBytes(int mcc, int mnc, int lac, int cellId, int strength) {

        ByteBuffer buff = ByteBuffer.allocate(LENGTH);
        buff.putShort((short) mcc);
        buff.putShort((short) mnc);
        buff.putInt(lac);
        buff.putInt(cellId);
        buff.putShort((short) strength);

        return buff.array();
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
