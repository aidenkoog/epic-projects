package io.github.aidenkoog.testdriver.message;

import io.github.aidenkoog.testdriver.message.v2.payload.Battery;
import io.github.aidenkoog.testdriver.message.v2.payload.CellInfo;
import io.github.aidenkoog.testdriver.message.v2.payload.Index;
import io.github.aidenkoog.testdriver.message.v2.payload.Location;
import io.github.aidenkoog.testdriver.message.v2.payload.PayloadEntity;
import io.github.aidenkoog.testdriver.message.v2.payload.Type;

import java.util.ArrayList;
import java.util.List;

public class RequestMessagePayload {

    private static final String TAG = RequestMessagePayload.class.getSimpleName();

    private List<PayloadEntity> mMessagePayload = new ArrayList<PayloadEntity>();

    public RequestMessagePayload() {
        mMessagePayload.add(new Battery());
    }

    public RequestMessagePayload(byte type, int index) {
        mMessagePayload.add(new Type(type));
        mMessagePayload.add(new Index(index));
        mMessagePayload.add(new Battery());
    }

    public void addCellTower(int mcc, int mnc, int lac, int cellId, int strength) {
        mMessagePayload.add(new CellInfo(mcc, mnc, lac, cellId, strength));
    }

    public void addLocation(double latitude, double longitude, float accuracy) {
        mMessagePayload.add(new Location(String.format("%f,%f,%f", latitude, longitude, accuracy)));
    }

    public int getLength() {
        int length = 0;
        for (PayloadEntity entity : mMessagePayload) {
            length += entity.getLength();
        }
        return length;
    }

    public byte[] getBytes() {
        int index = 0;
        byte[] b = new byte[getLength()];
        for (PayloadEntity entity : mMessagePayload) {
            System.arraycopy(entity.getBytes(), 0, b, index, entity.getLength());
            index += entity.getLength();
        }
        return b;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\t[PAYLOAD:");
        for (PayloadEntity entity : mMessagePayload) {
            buffer.append("\n\t\t");
            buffer.append(entity);
        }
        buffer.append("\n\t]");
        return buffer.toString();
    }
}
