package io.github.aidenkoog.testdriver.operator;

import static io.github.aidenkoog.testdriver.message.v2.header.Status.EMERGENCY_STATUS_MASK;
import static io.github.aidenkoog.testdriver.message.v2.header.Status.EMERGENCY_STATUS_SHIFT;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.ACK_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.IDLE_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TAKE_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TRIGGER_STATUS;

import android.os.Message;
import android.util.Log;

import io.github.aidenkoog.testdriver.message.MessageOperator;
import io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification;
import io.github.aidenkoog.testdriver.message.MessageOperator.Sent;
import io.github.aidenkoog.testdriver.message.ResponseMessage;
import io.github.aidenkoog.testdriver.message.auth.SecurityPacket;
import io.github.aidenkoog.testdriver.message.exception.MessageInvalidException;
import io.github.aidenkoog.testdriver.message.exception.PacketInvalidException;
import io.github.aidenkoog.testdriver.message.exception.PacketLackException;
import io.github.aidenkoog.testdriver.message.v2.payload.EmgStatus;
import io.github.aidenkoog.testdriver.message.v2.payload.PayloadEntity;
import io.github.aidenkoog.testdriver.network.SocketManager;
import io.github.aidenkoog.testdriver.service.ObserverService;
import io.github.aidenkoog.testdriver.system.PositionManager;
import io.github.aidenkoog.testdriver.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class OperatorManager implements Observer {

    private static final String TAG = OperatorManager.class.getSimpleName();

    private static final int MAX_WAIT_POSITION_WAIT_TIME = 2;

    private BlockingQueue<Message> mSendQueue = new LinkedBlockingQueue<Message>();
    private BlockingQueue<Message> mDispatcherQueue = new LinkedBlockingQueue<Message>();
    private BlockingQueue<Message> mPositionResponse =  new LinkedBlockingQueue<Message>();

    private List<Byte> mReceiveQueue = new ArrayList<Byte>();
    private OperatorList mOperators = new OperatorArrayList();

    private final Object QUEUE_INSTANCE_LOCK = new Object();
    private final Object MESSAGE_INSTANCE_LOCK = new Object();

    private ObserverService mObserverService = ObserverService.getInstance();

    private Status mStatus = IDLE_STATUS;

    public enum Status {
        IDLE_STATUS, TRIGGER_STATUS, ACK_STATUS, TAKE_STATUS
    }

    private OperatorManager() {
        mObserverService.addObserver(MessageOperator.class.getSimpleName(), this);

        new Thread(new Sender(mSendQueue)).start();
        new Thread(new MessageDispatcher(mDispatcherQueue)).start();
    }

    private static class SingletonData {
        private static final OperatorManager instance = new OperatorManager();
    }

    public static OperatorManager getInstance() {
        return SingletonData.instance;
    }

    public void eventSos() {
        addOperator(MessageOperator.obtainSos());
    }

    public void eventPowerOn() {
        addOperator(MessageOperator.obtainPowerOn());
    }

    public void clear() {  //  temp
        synchronized (MESSAGE_INSTANCE_LOCK) {
            mOperators.removeAll();
        }
    }

    public void addOperator(MessageOperator operator) {

        synchronized (MESSAGE_INSTANCE_LOCK) {
            if (operator.isPriority() == true) {
                if (mOperators.hasPriority() == true) {
                    //  Set Cancel
                    operator = mOperators.getPriority();
                    operator.setCancel();
                    return;
                }
            }
            mOperators.addOp(operator);
        }
    }

    @Override
    public void update(Observable observed, Object arg) {
        if (arg instanceof OperationNotification) {
            Status oldStatus = mStatus;
            synchronized (MESSAGE_INSTANCE_LOCK) {
                //  Just checking operation status for UI
                if (mOperators.isEmpty() == true) {
                    finishOperation();
                    mStatus = IDLE_STATUS;
                } else if (mOperators.hasPriority() == true) {
                    MessageOperator operator = mOperators.getPriority();
                    if (operator.isResolve() == true) {
                        mStatus = IDLE_STATUS;
                    } else if (operator.isTake() == true) {
                        mStatus = TAKE_STATUS;
                    } else if (operator.isAck() == true) {
                        mStatus = ACK_STATUS;
                    } else {
                        mStatus = TRIGGER_STATUS;
                    }
                } else {
                    mStatus = IDLE_STATUS;
                }
            }
            if (oldStatus != mStatus) {
                mObserverService.postNotification(TAG, mStatus);
            }
        } else if (arg instanceof Sent) {
            mSendQueue.offer(Message.obtain());
        }
    }

    //  Note. In the synchronized MESSAGE_INSTANCE_LOCK
    private void dispatchPayload(List<PayloadEntity> payloads) {
        for (PayloadEntity entity : payloads) {
            if (entity instanceof EmgStatus) {
                if (((EmgStatus)entity).get() == EmgStatus.TAKEN) {
                    mOperators.setTake();
                } else if (((EmgStatus)entity).get() == EmgStatus.RESOLVE) {
                    mOperators.setResolve();
                } else if (((EmgStatus)entity).get() == EmgStatus.CANCEL) {
                    //  Need to be added ?
                }
            }
        }
    }

    //  Note. In the synchronized MESSAGE_INSTANCE_LOCK
    private void dispatchStatus(byte[] status) {
        byte emergency = (byte)((Utils.byteArrayToNumeral(status)
                >> EMERGENCY_STATUS_SHIFT) & EMERGENCY_STATUS_MASK);
        if (emergency != 0) {
            byte sos = (byte)(emergency & 0B11);
            byte fall = (byte)(emergency & 0B1100);

            if (mOperators.hasPriority() == false) {
                MessageOperator operator = null;
                if (sos != 0) {
                    operator = MessageOperator.obtainSos();
                    if (sos == 0B10) {
                        operator.setTake();
                    }
                } else if (fall != 0) {
                    operator = MessageOperator.obtainFall();
                    if (fall == 0B1000) {
                        operator.setTake();
                    }
                }
                if (operator != null) {
                    operator.setIndex((byte)0x02);  //  Temporary.
                    operator.setAck();
                }

                if (operator != null) mOperators.addOp(operator);
            } else {
                MessageOperator operator = mOperators.getPriority();
                if (sos != 0) {
                    if (sos == 0B01) {
                        operator.setAck();
                    } else if (sos == 0B10) {
                        operator.setTake();
                    }
                } else if (fall != 0) {
                    if (fall == 0B1000) {
                        operator.setAck();
                    } else if (fall == 0B1000) {
                        operator.setTake();
                    }
                }
            }
        }
    }

    private void startOperation() {
        if (SocketManager.getInstance().socketOpened() == false) {
            SocketManager.getInstance().openSocket(socketManagerListener);
        }
        PositionManager.getInstance().startPositioning();
    }

    private void finishOperation() {
        SocketManager.getInstance().closeSocket();
        PositionManager.getInstance().stopPositioning();
    }

    SocketManager.OnSocketManagerListener socketManagerListener
                        = new SocketManager.OnSocketManagerListener() {
        @Override
        public void onSocketManagerRead(byte[] received) {
            //Log.e(TAG, "received:" + Utils.byteArrayToHex(received));

            synchronized (QUEUE_INSTANCE_LOCK) {
                for (byte b : received) {
                    mReceiveQueue.add(b);
                }
            }
            mDispatcherQueue.offer(Message.obtain());
        }

        @Override
        public void onSocketManagerClose() {
            Log.e(TAG, "closed");

            if (mOperators.isEmpty() == false) {
                SocketManager.getInstance().openSocket(socketManagerListener);
            }
        }
    };

    PositionManager.OnPositionManagerListener positionManagerListener
                        = new PositionManager.OnPositionManagerListener() {
        @Override
        public void onPositionReady() {
            mPositionResponse.offer(Message.obtain());
        }
    };

    class Sender implements Runnable {

        private BlockingQueue<Message> queue;

        public Sender(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {

            while(true) {
                try {
                    queue.take();

                    if (mOperators.isEmpty() == true) {
                        finishOperation();
                        continue;
                    }

                    startOperation();

                    OperatorList operators = null;
                    synchronized (MESSAGE_INSTANCE_LOCK) {
                        operators = mOperators.getOperatorsReadyToBeSent();
                    }

                    if (/*mOperators.hasPriority() == true
                            && */ PositionManager.getInstance().isPositionReady() == false) {
                        Log.e(TAG, "Wait 3 seconds until the position is ready");
                        mPositionResponse.poll(MAX_WAIT_POSITION_WAIT_TIME, TimeUnit.SECONDS);
                    } else {
                        mPositionResponse.clear();
                    }

                    for (MessageOperator operator : operators) {
                        if (operator.genRequestMessage() == true) {
                            byte[] bytes = operator.getBytes();
                            //Log.e(TAG, "bytes:" + Utils.byteArrayToHex(bytes));
                            Log.e(TAG, operator.toString());
                            if (SocketManager.getInstance().send(
                                    (new SecurityPacket(bytes)).getBytes()) == true) {
                                operator.setSent();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MessageDispatcher implements Runnable {

        private BlockingQueue<Message> queue;

        public MessageDispatcher(BlockingQueue<Message> queue) {
            this.queue = queue;
        }

        private List<Byte> getValidSecurityPacket()
            throws PacketLackException, PacketInvalidException {

            List<Byte> validSecurityPacket = new ArrayList<Byte>();
            int validSecurityPacketLen =
                SecurityPacket.getValidSecurityPacketLen(mReceiveQueue);

            for (int i = 0; i < validSecurityPacketLen; i++) {
                validSecurityPacket.add(mReceiveQueue.remove(0));
            }
            return validSecurityPacket;
        }

        private void operateMessage(ResponseMessage message) {
            if (message.isResponse() == true) {
                mOperators.setAck(message.getMessageId());
            } else if (message.isError()) {
                //  How to handle it
            }
            dispatchPayload(message.getPayloadList());
            dispatchStatus(message.getStatus());

            //  Remove to be ready
            mOperators.removeReadyToBe();
        }

        @Override
        public void run() {

            while (true) {
                try {
                    List<Byte> securityPacket = null;

                    queue.take();

                    synchronized (QUEUE_INSTANCE_LOCK) {
                        try {
                            securityPacket = getValidSecurityPacket();
                        } catch (PacketLackException e) {
                            //  Just wait until receving next buffer
                            e.printStackTrace();
                        } catch (PacketInvalidException e) {
                            //  Now reset
                            mReceiveQueue.clear();
                            //  TODO searching CRYPTO_HS_AAD and check buffer again
                            e.printStackTrace();
                        }
                    }

                    if (mReceiveQueue.size() > 0) {
                        mDispatcherQueue.offer(Message.obtain());
                    }

                    try {
                        if (securityPacket != null) {
                            ResponseMessage message = new ResponseMessage(
                                (new SecurityPacket(securityPacket)).getDecryptBytes());

                            //Log.e(TAG, "ResponseMessage:" + Utils.byteArrayToHex(message.getBytes()));
                            Log.e(TAG, message.toString());

                            synchronized (MESSAGE_INSTANCE_LOCK) {
                                operateMessage(message);
                            }
                        }
                    } catch (PacketInvalidException e) {
                        e.printStackTrace();
                    } catch (MessageInvalidException e) {
                        e.printStackTrace();
                    }

                    if (mOperators.isEmpty() == true) {
                        finishOperation();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

