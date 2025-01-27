package io.github.aidenkoog.testdriver.message.v1.payload;

import android.util.Log;

public class CellInfo extends PayloadEntity {

    private static final String TAG = CellInfo.class.getSimpleName();

    private static final int LENGTH = 30;

    private int mcc;
    private int mnc;
    private int lac;
    private int cellId;
    private int strengh;

    public CellInfo() {
        setType(PAYLOAD_TYPE_CELL_INFORMATION);
    }

    public CellInfo(String cellTower) {
        this();
        setValue(stringToByteArrayWithCount(cellTower, LENGTH));
    }

    public String toString() {
        return super.toString(new String(getValue()));
    }
}
