package io.github.aidenkoog.testdriver.message;

import io.github.aidenkoog.testdriver.system.PositionManager;

public class RequestMessage {

    private static final String TAG = RequestMessage.class.getSimpleName();

    MessageHeader mHeader;
    RequestMessagePayload mPayload;

    public RequestMessage(Part part, int index, byte[] messageId) {

        mHeader = new MessageHeader(part, messageId);
        mPayload = new RequestMessagePayload(part.getType(), index);

        /* For Saving Google expense List<CellInfoEx> cellInfoEx = CellTowerManager.getInstance().getCellTowerInfo();
        if (cellInfoEx.size() > 0) {
            for (CellInfoEx info : cellInfoEx) {
                mPayload.addCellTower(info.getMcc(), info.getMnc(),
                        info.getLac(), info.getCellId(), info.getStrength());
            }
        }*/

        if (PositionManager.getInstance().getLatitude() != 0
                && PositionManager.getInstance().getLongitude() != 0) {
            mPayload.addLocation(PositionManager.getInstance().getLatitude(),
                PositionManager.getInstance().getLongitude(),
                PositionManager.getInstance().getAccuracy());
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
