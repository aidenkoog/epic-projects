package io.github.aidenkoog.testdriver.message;

import android.util.Log;

import io.github.aidenkoog.testdriver.system.CellTowerManager;
import io.github.aidenkoog.testdriver.system.CellTowerManager.CellInfoEx;
import io.github.aidenkoog.testdriver.system.PogoManager;
import io.github.aidenkoog.testdriver.system.PositionManager;
import io.github.aidenkoog.testdriver.utils.Utils;

import java.util.List;

public class RequestMessage {

    private static final String TAG = RequestMessage.class.getSimpleName();

    RequestMessageHeader mHeader;
    RequestMessagePayload mPayload;

    public RequestMessage(byte alert, byte index, byte[] messageId,
                        String eventStartTime, String eventSentTime,
                        String takeReceiveTime, String resolveReceiveTime) {

        mHeader = new RequestMessageHeader(alert, messageId);
        mPayload = new RequestMessagePayload(index);

        if (PogoManager.getInstance().getPogoCount() > 0) {
            String address = PogoManager.getInstance().getPogoAddress();
            if (address != null) {
                mPayload.addBeacon(address.replace(":", ""));  //  Remove colon
            }
        }

        List<CellInfoEx> cellInfoEx = CellTowerManager.getInstance().getCellTowerInfo();

        if (cellInfoEx.size() > 0) {
            for (CellInfoEx info : cellInfoEx) {
                mPayload.addCellTower(info.toString());
            }
        }

        if (PositionManager.getInstance().getLatitude() != 0
                && PositionManager.getInstance().getLongitude() != 0) {
            Log.e(TAG, "lati:" + PositionManager.getInstance().getLatitude());
            Log.e(TAG, "long:" + PositionManager.getInstance().getLongitude());
            mPayload.addLocation(Double.toString(PositionManager.getInstance().getLatitude()),
                Double.toString(PositionManager.getInstance().getLongitude()));
        }

        if (eventStartTime != null) {
            mPayload.addEventStartTime(eventStartTime);
        }

        if (eventSentTime != null) {
            mPayload.addEventSentTime(eventSentTime);
        }

        if (takeReceiveTime != null) {
            mPayload.addTakeReceiveTime(takeReceiveTime);
        }

        if (resolveReceiveTime != null) {
            mPayload.addResolveReceiveTime(resolveReceiveTime);
        }
    }

    public byte[] getHeader(short payloadLen) {
        //  Must be set payload length before getting data
        mHeader.setPayloadLen((short)payloadLen);

        return mHeader.getBytes();
    }

    public byte[] getBytes() {
        byte[] payload = mPayload.getBytes();
        byte[] header = getHeader((short)payload.length);
        byte[] b = new byte[header.length + payload.length];
        System.arraycopy(header, 0, b, 0, header.length);
        System.arraycopy(payload, 0, b, header.length, payload.length);
        return b;
    }

    public String toString() {
        return "RequestMessage:[" + mHeader.toString() + mPayload.toString() + "\n]";
    }
}
