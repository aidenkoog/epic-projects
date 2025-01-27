package io.github.aidenkoog.testdriver.message;

import android.util.Log;

import io.github.aidenkoog.testdriver.message.exception.*;
import io.github.aidenkoog.testdriver.message.v1.header.*;
import io.github.aidenkoog.testdriver.message.v1.payload.PayloadEntity;
import io.github.aidenkoog.testdriver.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ResponseMessage {

    private static final String TAG = ResponseMessage.class.getSimpleName();

    private Version version;
    private Command command;
    private MessageId messageId;
    private SupportField supportField;
    private PayloadLength payloadLength;
    private List<PayloadEntity> mPayloadList = new ArrayList<PayloadEntity>();

    public byte[] getmessageId() {
        return messageId.getValue();
    }

    public ResponseMessage(byte[] data) throws MessageInvalidException {
        byte[] buf = null;

        try {
            List<Byte> dataList = Utils.byteArrayToArrayList(data);

            version = new Version();
            command = new Command();
            messageId = new MessageId();
            supportField = new SupportField();
            payloadLength = new PayloadLength();

            version.setValue(dataList.remove(0));
            command.setValue(dataList.remove(0));

            if (dataList.size() < MessageId.LENGTH) {
                throw new MessageInvalidException("Lack of data");
            }
            buf = new byte[MessageId.LENGTH];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = dataList.remove(0);
            }
            messageId.setValue(buf);

            //  Protocal design fault
            if (isAction() == true) {
                return;
            }
            supportField.setValue(dataList.remove(0));

            if (dataList.size() < PayloadLength.LENGTH) {
                throw new MessageInvalidException("Lack of data");
            }
            buf = new byte[PayloadLength.LENGTH];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = dataList.remove(0);
            }
            payloadLength.setValue(buf);

            PayloadEntity.parsePayload(dataList, mPayloadList);

        } catch (IndexOutOfBoundsException e) {
            throw new MessageInvalidException("Message parsing error!");
        }
    }

    public boolean isAction() {
        return command.isAction();
    }

    public boolean isRequest() {
        return command.isRequest();
    }

    public boolean isResponse() {
        return command.isResponse();
    }

    public boolean isCancel() {
        return command.isCancel();
    }

    public boolean isTake() {
        return command.isTake();
    }

    public boolean isResolve() {
        return command.isResolve();
    }

    public List<PayloadEntity> getPayloadList() {
        return mPayloadList;
    }

    public int getHeaderLength() {
        return version.getLength()
                + command.getLength()
                + messageId.getLength()
                + supportField.getLength()
                + payloadLength.getLength();
    }

    public byte[] getHeader() {
        int i = 0;
        byte[] b = new byte[getHeaderLength()];

        System.arraycopy(version.getValue(),
                0, b, i, version.getLength());
        i += version.getLength();

        System.arraycopy(command.getValue(),
                0, b, i, command.getLength());
        i += command.getLength();

        System.arraycopy(messageId.getValue(),
                0, b, i, messageId.getLength());
        i += messageId.getLength();

        System.arraycopy(supportField.getValue(),
                0, b, i, supportField.getLength());
        i += supportField.getLength();

        System.arraycopy(payloadLength.getValue(),
                0, b, i, payloadLength.getLength());
        i += payloadLength.getLength();

        return b;
    }

    public byte[] getPayloads() {
        int index = 0;
        byte[] b = new byte[getPayloadsLength()];
        for (PayloadEntity entity : mPayloadList) {
            System.arraycopy(entity.getBytes(), 0, b, index, entity.getLength());
            index += entity.getLength();
        }
        return b;
    }

    public int getPayloadsLength() {
        int length = 0;
        for (PayloadEntity entity : mPayloadList) {
            length += entity.getLength();
        }
        return length;
    }

    public byte[] getBytes() {
        byte[] payload = getPayloads();
        byte[] header = getHeader();

        byte[] b = new byte[header.length + payload.length];

        System.arraycopy(header, 0, b, 0, header.length);
        System.arraycopy(payload, 0, b, header.length, payload.length);

        return b;
    }

    public String toHeaderString() {
        return "\n\t[HEADER:"
            + "\n\t\t[" + "Version:" + version + "]"
            + "\n\t\t[" + "Command:" + command + "]"
            + "\n\t\t[" + "MessageId:" + messageId + "]"
            + "\n\t\t[" + "SupportField:" + supportField + "]"
            + "\n\t\t[" + "PayloadLength:" + payloadLength + "]"
            + "\n\t]";
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
