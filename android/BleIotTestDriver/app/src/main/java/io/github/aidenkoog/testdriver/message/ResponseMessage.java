package io.github.aidenkoog.testdriver.message;

import io.github.aidenkoog.testdriver.message.exception.MessageInvalidException;
import io.github.aidenkoog.testdriver.message.v2.payload.PayloadEntity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ResponseMessage {

    private static final String TAG = ResponseMessage.class.getSimpleName();

    MessageHeader mHeader;
    private List<PayloadEntity> mPayloadList = new ArrayList<PayloadEntity>();

    public byte[] getMessageId() {
        return mHeader.getMessageId();
    }

    public byte[] getStatus() {
        return mHeader.getStatus();
    }

    public ResponseMessage(byte[] data) throws MessageInvalidException {
        ByteBuffer buff = ByteBuffer.wrap(data);
        mHeader = new MessageHeader(buff);

        if (buff.remaining() < mHeader.getPayloadLength()) {
            throw new MessageInvalidException("Lack of payload data");
        }
        PayloadEntity.parsePayload(buff, mPayloadList);
    }

    public boolean isRequest() {
        return mHeader.isRequest();
    }

    public boolean isResponse() {
        return mHeader.isResponse();
    }

    public boolean isError() {
        return mHeader.isError();
    }

    public List<PayloadEntity> getPayloadList() {
        return mPayloadList;
    }

    public String toHeaderString() {
        return mHeader.toString();
    }

    public String toPayloadString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\t[PAYLOAD:");
        for (PayloadEntity entity : mPayloadList) {
            buffer.append("\n\t\t");
            buffer.append(entity);
        }
        buffer.append("\n\t]");
        return buffer.toString();
    }

    public String toString() {
        return "ResponseMessage:[" + toHeaderString() + toPayloadString() + "\n]";
    }
}
