package io.github.aidenkoog.testdriver.message;

import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.ACK;
import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.ADDED;
import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.CANCEL;
import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.REMOVED;
import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.RESOLVE;
import static io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification.TAKE;
import static io.github.aidenkoog.testdriver.message.MessageOperator.Sent.READY;
import static io.github.aidenkoog.testdriver.message.MessageOperator.Sent.RES;
import static io.github.aidenkoog.testdriver.message.MessageOperator.Sent.SENT;
import static io.github.aidenkoog.testdriver.message.Notification.FALL;
import static io.github.aidenkoog.testdriver.message.Notification.POWER_ON;
import static io.github.aidenkoog.testdriver.message.Notification.SOS;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import io.github.aidenkoog.testdriver.message.v2.header.MessageId;
import io.github.aidenkoog.testdriver.service.ObserverService;

import java.util.concurrent.TimeUnit;

public class MessageOperator {

    private static final String TAG = MessageOperator.class.getSimpleName();

    private static final int MSG_NO_SENT_RETRY = 0x0001;
    private static final int MSG_LOCATION_UPDATE = 0x0002;

    private static final long NO_SENT_RETRY_TIME = TimeUnit.SECONDS.toMillis(60);
    private static final long LOCATION_UPDATE_TIME = TimeUnit.MINUTES.toMillis(3);

    private int mIndex;
    private boolean mIsAck;
    private boolean mIsTake;
    private boolean mIsResolve;

    private Part mPart;

    private byte[] mMessageId;

    private Handler noSentRetryTimer;
    private Handler locationUpdateTimer;

    private RequestMessage mRequestMessage;

    public enum Sent {
        READY, SENT, RES
    }

    public enum OperationNotification {
        ACK, TAKE, RESOLVE, CANCEL, ADDED, REMOVED
    }

    private Sent mSent;
    private OperationNotification mOperationNotification;

    private void initHandler() {
        noSentRetryTimer = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "noSentRetryTimer handler");
                Log.e(TAG, toOperatorString());
                this.toString();
                switch (msg.what) {
                    case MSG_NO_SENT_RETRY:
                        mSent = READY;
                        refreshNoSentRetryTimer();
                        postReadyToSendNotification();
                        break;
                }
            }
        };
        locationUpdateTimer = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "locationUpdateTimer handler");
                Log.e(TAG, toOperatorString());
                this.toString();
                switch (msg.what) {
                    case MSG_LOCATION_UPDATE:
                        mSent = READY;
                        refreshLocationUpdateTimer();
                        postReadyToSendNotification();
                        break;
                }
            }
        };
    }

    private void refreshNoSentRetryTimer() {
        if (noSentRetryTimer != null) {
            noSentRetryTimer.removeCallbacksAndMessages(null);
            noSentRetryTimer.sendEmptyMessageDelayed(MSG_NO_SENT_RETRY, NO_SENT_RETRY_TIME);
        }
    }

    private void cancelNoSentRetryTimer() {
        if (noSentRetryTimer != null) {
            noSentRetryTimer.removeCallbacksAndMessages(null);
        }
    }

    private void refreshLocationUpdateTimer() {
        if (locationUpdateTimer != null) {
            locationUpdateTimer.removeCallbacksAndMessages(null);
            locationUpdateTimer.sendEmptyMessageDelayed(MSG_LOCATION_UPDATE, LOCATION_UPDATE_TIME);
        }
    }

    private void cancelLocationUpdateTimer() {
        if (locationUpdateTimer != null) {
            locationUpdateTimer.removeCallbacksAndMessages(null);
        }
    }

    private void initOperator(Part part) {
        mIndex = 0x01;
        mIsAck = false;
        mIsTake = false;
        mIsResolve = false;
        mPart = part;
        mMessageId = MessageId.genMessageId();
    }

    /*public MessageOperator(byte part, byte type) {
        initHandler();
        initOperator(new Event(part, type));
    }*/

    public MessageOperator(Part part) {
        initHandler();
        initOperator(part);
    }

    public static MessageOperator obtain(Part part) {
        return new MessageOperator(part);
    }

    public static MessageOperator obtainSos() {
        return obtain(new Notification(SOS));
    }

    public static MessageOperator obtainFall() {
        return obtain(new Notification(FALL));
    }

    public static MessageOperator obtainPowerOn() {
        return obtain(new Notification(POWER_ON));
    }

    public void added() {
        mSent = READY;
        postReadyToSendNotification();
        refreshNoSentRetryTimer();

        postOperationNotification(ADDED);
    }

    public void setSent() {
        mSent = SENT;
        mIndex++;

        refreshNoSentRetryTimer();
    }

    public void setAck() {
        mIsAck = true;

        mSent = RES;
        cancelNoSentRetryTimer();

        if (isPriority() == true) {
            refreshLocationUpdateTimer();
        }
        postOperationNotification(ACK);
    }

    public void setTake() {
        mIsTake = true;
        postOperationNotification(TAKE);
    }

    public void setResolve() {
        mIsResolve = true;
        postOperationNotification(RESOLVE);
    }

    public void setCancel() {
        mIndex = 0x01;
        mPart.setCancel();

        postOperationNotification(CANCEL);

        mSent = READY;
        cancelLocationUpdateTimer();
        refreshNoSentRetryTimer();
        postReadyToSendNotification();
    }

    public void removed() {
        postOperationNotification(REMOVED);

        cancelNoSentRetryTimer();
        cancelLocationUpdateTimer();
    }

    public boolean isReadyToBeRemoved() {
        if (isPriority() == false) {
            return isAck();
        } else {
            return isResolve();
        }
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public byte[] getMessageId() {
        return mMessageId;
    }

    public boolean isAck() {
        return mIsAck;
    }

    public boolean isTake() {
        return mIsTake;
    }

    public boolean isResolve() {
        return mIsResolve;
    }

    public boolean isReadyToBeSent() {
        if (mSent == READY) {
            return true;
        }
        return false;
    }

    public boolean isPriority() {
        return mPart.isPriority();
    }

    private void postOperationNotification(OperationNotification noti) {
        mOperationNotification = noti;
        ObserverService.getInstance().postNotification(TAG, noti);
    }

    private void postReadyToSendNotification() {
        mIsAck = false;
        ObserverService.getInstance().postNotification(TAG, READY);
    }

    public boolean genRequestMessage() {
        mRequestMessage = new RequestMessage(mPart, mIndex, mMessageId);
        return true;
    }

    public byte[] getBytes() {
        if (mRequestMessage == null) {
            genRequestMessage();
        }
        return mRequestMessage.getBytes();
    }

    public String toOperatorString() {
        return "\n[MessageOperator:"
            + "\n\t[" + "mIndex:" + mIndex + "]"
            + "\n\t[" + "mIsAck:" + mIsAck + "]"
            + "\n\t[" + "mIsTake:" + mIsTake + "]"
            + "\n\t[" + "mIsResolve:" + mIsResolve + "]"
            + "\n\t[" + "mSent:" + mSent + "]"
            + "\n\t[" + "mOperationNotification:" + mOperationNotification + "]"
            + "\n]";
    }

    public String toString() {
        if (mRequestMessage == null) {
            genRequestMessage();
        }
        return "\n" + mRequestMessage.toString();
    }
}
