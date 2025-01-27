package io.github.aidenkoog.testdriver.message;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;

import io.github.aidenkoog.testdriver.BleIoT;
import io.github.aidenkoog.testdriver.message.v1.header.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RequestMessageHeader {

    private static final String TAG = RequestMessageHeader.class.getSimpleName();

    private Version version;
    private Command command;
    private Serial  serial;
    private MessageId messageId;
    private FirmwareVersion firmwareVersion;
    private ModemVersion modemVersion;
    private SupportField supportField;
    private PayloadLength payloadLength;

    public RequestMessageHeader(byte alert) {
        version = new Version();
        command = new Command(alert);
        serial = new Serial();
        messageId = new MessageId();
        firmwareVersion = new FirmwareVersion();
        modemVersion = new ModemVersion();
        supportField = new SupportField();
        payloadLength = new PayloadLength();
    }

    public RequestMessageHeader(byte alert, byte[] messageId) {
        this.version = new Version();
        this.command = new Command(alert);
        this.serial = new Serial();
        this.messageId = new MessageId(messageId);
        this.firmwareVersion = new FirmwareVersion();
        this.modemVersion = new ModemVersion();
        this.supportField = new SupportField();
        this.payloadLength = new PayloadLength();
    }

    public void setPayloadLen(short len) {
        payloadLength.setValue(len);
    }

    public int getLength() {

        return version.getLength()
                + command.getLength()
                + serial.getLength()
                + messageId.getLength()
                + firmwareVersion.getLength()
                + modemVersion.getLength()
                + supportField.getLength()
                + payloadLength.getLength();
    }

    public byte[] getBytes() {

        int i = 0;
        byte[] b = new byte[getLength()];

        System.arraycopy(version.getValue(), 0, b, i, version.getLength());
        i += version.getLength();

        System.arraycopy(command.getValue(), 0, b, i, command.getLength());
        i += command.getLength();

        System.arraycopy(serial.getValue(), 0, b, i, serial.getLength());
        i += serial.getLength();

        System.arraycopy(messageId.getValue(), 0, b, i, messageId.getLength());
        i += messageId.getLength();

        System.arraycopy(firmwareVersion.getValue(), 0, b, i, firmwareVersion.getLength());
        i += firmwareVersion.getLength();

        System.arraycopy(modemVersion.getValue(), 0, b, i, modemVersion.getLength());
        i += modemVersion.getLength();

        System.arraycopy(supportField.getValue(), 0, b, i, supportField.getLength());
        i += supportField.getLength();

        System.arraycopy(payloadLength.getValue(), 0, b, i, payloadLength.getLength());
        i += payloadLength.getLength();

        return b;
    }

    public String toString() {
        return "\n\t[HEADER:"
            + "\n\t\t[" + "Version:" + version + "]"
            + "\n\t\t[" + "Command:" + command + "]"
            + "\n\t\t[" + "Serial:" + serial + "]"
            + "\n\t\t[" + "MessageId:" + messageId + "]"
            + "\n\t\t[" + "FirmwareVersion:" + firmwareVersion + "]"
            + "\n\t\t[" + "ModemVersion:" + modemVersion + "]"
            + "\n\t\t[" + "SupportField:" + supportField + "]"
            + "\n\t\t[" + "PayloadLength:" + payloadLength + "]"
            + "\n\t]";
    }
}
