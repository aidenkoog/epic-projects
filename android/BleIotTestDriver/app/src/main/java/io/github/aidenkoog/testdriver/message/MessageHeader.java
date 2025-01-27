package io.github.aidenkoog.testdriver.message;

import io.github.aidenkoog.testdriver.message.v2.header.Age;
import io.github.aidenkoog.testdriver.message.v2.header.Cmd;
import io.github.aidenkoog.testdriver.message.v2.header.Date;
import io.github.aidenkoog.testdriver.message.v2.header.FirmwareVersion;
import io.github.aidenkoog.testdriver.message.v2.header.Gender;
import io.github.aidenkoog.testdriver.message.v2.header.Height;
import io.github.aidenkoog.testdriver.message.v2.header.MessageId;
import io.github.aidenkoog.testdriver.message.v2.header.PayloadLength;
import io.github.aidenkoog.testdriver.message.v2.header.Reminder;
import io.github.aidenkoog.testdriver.message.v2.header.Ret;
import io.github.aidenkoog.testdriver.message.v2.header.Sequence;
import io.github.aidenkoog.testdriver.message.v2.header.Serial;
import io.github.aidenkoog.testdriver.message.v2.header.SourceIp;
import io.github.aidenkoog.testdriver.message.v2.header.Status;
import io.github.aidenkoog.testdriver.message.v2.header.Version;
import io.github.aidenkoog.testdriver.message.v2.header.Weight;

import java.nio.ByteBuffer;

public class MessageHeader {

    private static final String TAG = MessageHeader.class.getSimpleName();

    private Version version;
    private Serial  serial;
    private MessageId messageId;
    private Sequence sequence;
    private Status status;
    private Cmd cmd;
    private Ret ret;
    private SourceIp sourceIp;
    private Height height;
    private Weight weight;
    private Gender gender;
    private Age age;
    private Reminder reminder;
    private Date date;
    private FirmwareVersion firmwareVersion;
    private PayloadLength payloadLength;

    public MessageHeader(Part part, byte[] messageId) {
        this.version = new Version();
        this.serial = new Serial();
        this.messageId = new MessageId(messageId);
        this.sequence = new Sequence();
        this.status = new Status();
        this.cmd = new Cmd(part.getPart());
        this.ret = new Ret(Ret.REQUEST);
        this.sourceIp = new SourceIp();
        this.height = new Height();
        this.weight = new Weight();
        this.gender = new Gender();
        this.age = new Age();
        this.reminder = new Reminder();
        this.date = new Date();
        this.firmwareVersion = new FirmwareVersion();
        this.payloadLength = new PayloadLength();
    }

    public MessageHeader(ByteBuffer buff) {
        this.version = new Version(buff);
        this.serial = new Serial(buff);
        this.messageId = new MessageId(buff);
        this.sequence = new Sequence(buff);
        this.status = new Status(buff);
        this.cmd = new Cmd(buff);
        this.ret = new Ret(buff);
        this.sourceIp = new SourceIp(buff);
        this.height = new Height(buff);
        this.weight = new Weight(buff);
        this.gender = new Gender(buff);
        this.age = new Age(buff);
        this.reminder = new Reminder(buff);
        this.date = new Date(buff);
        this.firmwareVersion = new FirmwareVersion(buff);
        this.payloadLength = new PayloadLength(buff);
    }

    public void setPayloadLen(short len) {
        payloadLength.setValue(len);
    }

    public int getLength() {
        return version.getLength()
                + serial.getLength()
                + messageId.getLength()
                + sequence.getLength()
                + status.getLength()
                + cmd.getLength()
                + ret.getLength()
                + sourceIp.getLength()
                + height.getLength()
                + weight.getLength()
                + gender.getLength()
                + age.getLength()
                + reminder.getLength()
                + date.getLength()
                + firmwareVersion.getLength()
                + payloadLength.getLength();
    }

    public byte[] getMessageId() {
        return messageId.getValue();
    }

    public byte[] getStatus() {
        return status.getValue();
    }

    public int getPayloadLength() {
        return payloadLength.get();
    }

    public boolean isRequest() {
        return ret.get() == Ret.REQUEST;
    }

    public boolean isResponse() {
        return ret.get() == Ret.RESPONSE;
    }

    public boolean isError() {
        return ret.get() == Ret.ERROR;
    }

    public byte[] getBytes() {

        int i = 0;
        byte[] b = new byte[getLength()];

        System.arraycopy(version.getValue(), 0, b, i, version.getLength());
        i += version.getLength();

        System.arraycopy(serial.getValue(), 0, b, i, serial.getLength());
        i += serial.getLength();

        System.arraycopy(messageId.getValue(), 0, b, i, messageId.getLength());
        i += messageId.getLength();

        System.arraycopy(sequence.getValue(), 0, b, i, sequence.getLength());
        i += sequence.getLength();

        System.arraycopy(status.getValue(), 0, b, i, status.getLength());
        i += status.getLength();

        System.arraycopy(cmd.getValue(), 0, b, i, cmd.getLength());
        i += cmd.getLength();

        System.arraycopy(ret.getValue(), 0, b, i, ret.getLength());
        i += ret.getLength();

        System.arraycopy(sourceIp.getValue(), 0, b, i, sourceIp.getLength());
        i += sourceIp.getLength();

        System.arraycopy(height.getValue(), 0, b, i, height.getLength());
        i += height.getLength();

        System.arraycopy(weight.getValue(), 0, b, i, weight.getLength());
        i += weight.getLength();

        System.arraycopy(gender.getValue(), 0, b, i, gender.getLength());
        i += gender.getLength();

        System.arraycopy(age.getValue(), 0, b, i, age.getLength());
        i += age.getLength();

        System.arraycopy(reminder.getValue(), 0, b, i, reminder.getLength());
        i += reminder.getLength();

        System.arraycopy(date.getValue(), 0, b, i, date.getLength());
        i += date.getLength();

        System.arraycopy(firmwareVersion.getValue(), 0, b, i, firmwareVersion.getLength());
        i += firmwareVersion.getLength();

        System.arraycopy(payloadLength.getValue(), 0, b, i, payloadLength.getLength());
        i += payloadLength.getLength();

        return b;
    }

    public String toString() {
        return "\n\t[HEADER:"
            + "\n\t\t[" + "Version:" + version + "]"
            + "\n\t\t[" + "Serial:" + serial + "]"
            + "\n\t\t[" + "MessageId:" + messageId + "]"
            + "\n\t\t[" + "sequence:" + sequence + "]"
            + "\n\t\t[" + "status:" + status + "]"
            + "\n\t\t[" + "cmd:" + cmd + "]"
            + "\n\t\t[" + "ret:" + ret + "]"
            + "\n\t\t[" + "sourceIp:" + sourceIp + "]"
            + "\n\t\t[" + "height:" + height + "]"
            + "\n\t\t[" + "weight:" + weight + "]"
            + "\n\t\t[" + "gender:" + gender + "]"
            + "\n\t\t[" + "age:" + age + "]"
            + "\n\t\t[" + "reminder:" + reminder + "]"
            + "\n\t\t[" + "date:" + date + "]"
            + "\n\t\t[" + "FirmwareVersion:" + firmwareVersion + "]"
            + "\n\t\t[" + "PayloadLength:" + payloadLength + "]"
            + "\n\t]";
    }
}
