package io.github.aidenkoog.testdriver.message;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;

import io.github.aidenkoog.testdriver.message.v1.header.*;
import io.github.aidenkoog.testdriver.message.v1.payload.*;
import io.github.aidenkoog.testdriver.utils.Utils;
import io.github.aidenkoog.testdriver.BleIoT;

import java.util.ArrayList;
import java.util.List;

public class RequestMessagePayload {

    private static final String TAG = RequestMessagePayload.class.getSimpleName();

    private List<PayloadEntity> mMessagePayload = new ArrayList<PayloadEntity>();

    public RequestMessagePayload() {
        mMessagePayload.add(new Battery());
    }

    public RequestMessagePayload(byte index) {
        mMessagePayload.add(new Index(index));
        mMessagePayload.add(new Battery());
    }

    public void addBeacon(String address) {
        mMessagePayload.add(new Beacon(address));
    }

    public void addCellTower(String cellTower) {
        mMessagePayload.add(new CellInfo(cellTower));
    }

    public void addLocation(String latitude, String longitude) {
        mMessagePayload.add(new Latitude(latitude));
        mMessagePayload.add(new Longitude(longitude));
    }

    public void addEventStartTime(String time) {
        mMessagePayload.add(new EventStartTime(time));
    }

    public void addEventSentTime(String time) {
        mMessagePayload.add(new EventSentTime(time));
    }

    public void addTakeReceiveTime(String time) {
        mMessagePayload.add(new TakeReceiveTime(time));
    }

    public void addResolveReceiveTime(String time) {
        mMessagePayload.add(new ResolveReceiveTime(time));
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
