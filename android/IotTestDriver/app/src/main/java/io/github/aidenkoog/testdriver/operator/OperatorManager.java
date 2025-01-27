package io.github.aidenkoog.testdriver.operator;

import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

//  Consider to be removed
import io.github.aidenkoog.testdriver.message.v1.payload.*;

import io.github.aidenkoog.testdriver.message.auth.SecurityPacket;
import io.github.aidenkoog.testdriver.message.exception.*;
import io.github.aidenkoog.testdriver.message.MessageOperator;
import io.github.aidenkoog.testdriver.message.MessageOperator.OperationNotification;
import io.github.aidenkoog.testdriver.message.MessageOperator.Sent;
import io.github.aidenkoog.testdriver.message.ResponseMessage;
import io.github.aidenkoog.testdriver.network.SocketManager;
import io.github.aidenkoog.testdriver.service.ObserverService;
import io.github.aidenkoog.testdriver.system.PogoManager;
import io.github.aidenkoog.testdriver.system.PositionManager;
import io.github.aidenkoog.testdriver.utils.Utils;

import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.IDLE_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TRIGGER_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.ACK_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TAKE_STATUS;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class OperatorManager implements Observer {

    private static final String TAG = OperatorManager.class.getSimpleName();

    private static final int MAX_WAIT_POGO_WAIT_TIME = 3;
    private static final int MAX_WAIT_POSITION_WAIT_TIME = 2;

    private BlockingQueue<Message> mSendQueue = new LinkedBlockingQueue<Message>();
    private BlockingQueue<Message> mDispatcherQueue = new LinkedBlockingQueue<Message>();
    private BlockingQueue<Message> mPogoResponse =  new LinkedBlockingQueue<Message>();
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
    private void dispatchServerStatus(ServerStatus serverStatus) {
        if (mOperators.hasPriority() == false) {
            if (serverStatus.isPriority() == true) {
                MessageOperator operator = null;
                if (serverStatus.isSos() == true) {
                    operator = MessageOperator.obtainSos();
                } else if (serverStatus.isFall() == true) {
                    operator = MessageOperator.obtainFall();
                }
                if (operator != null) {
                    operator.setIndex((byte)0x02);  //  Temporary.
                    operator.setAck();
                }
                if (serverStatus.isTake() == true) {
                    if (operator != null) operator.setTake();
                }
                if (serverStatus.isResolve() == true) {
                    //  Do not need to be added
                } else {
                    if (operator != null) mOperators.addOp(operator);
                }
            }
        } /* Temp blocked else {
            MessageOperator operator = mOperators.getPriority();
            assert operator != null;
            if ((serverStatus & SERVER_STATUS_TAKECARE) == SERVER_STATUS_TAKECARE) {
                operator.setTake();
            }
            if ((serverStatus & SERVER_STATUS_RESOLVE) == SERVER_STATUS_RESOLVE) {
                mOperators.removeOp(operator);
            }
        } */
    }

    //  Note. In the synchronized MESSAGE_INSTANCE_LOCK 
    private void dispatchPayload(List<PayloadEntity> payloads) {
        for (PayloadEntity entity : payloads) {
            if (entity instanceof Date) {
                Log.e(TAG, "Date:" + entity);
            } else if (entity instanceof ServerStatus) {
                Log.e(TAG, "ServerStatus:" + entity);
                dispatchServerStatus((ServerStatus)entity);
            } else if (entity instanceof FirmwareUpdateStatus) {
                Log.e(TAG, "FirmwareUpdateStatus");
            } else if (entity instanceof FirmwareUpdateVersion) {
                Log.e(TAG, "FirmwareUpdateVersion");
            } else if (entity instanceof FirmwareTotalSize) {
                Log.e(TAG, "FirmwareTotalSize");
            } else if (entity instanceof FirmwareOffset) {
                Log.e(TAG, "FirmwareOffset");
            } else if (entity instanceof FirmwareLength) {
                Log.e(TAG, "FirmwareLength");
            } else if (entity instanceof FirmwareData) {
                Log.e(TAG, "FirmwareData");
            }
        }
    }

    private void startOperation() {
        if (SocketManager.getInstance().socketOpened() == false) {
            SocketManager.getInstance().openSocket(socketManagerListener);
        }
        PogoManager.getInstance().startScan(pogoManagerListener);
        PositionManager.getInstance().startPositioning();
    }

    private void finishOperation() {
        SocketManager.getInstance().closeSocket();
        PositionManager.getInstance().stopPositioning();
        PogoManager.getInstance().stopScan();
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

    PogoManager.OnPogoManagerListener pogoManagerListener
                        = new PogoManager.OnPogoManagerListener() {
        @Override
        public void onPogoReady() {
            mPogoResponse.offer(Message.obtain());
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
                            && */ PogoManager.getInstance().isPogoReady() == false) {
                        Log.e(TAG, "Wait 3 seconds until the pogo is ready");
                        mPogoResponse.poll(MAX_WAIT_POGO_WAIT_TIME, TimeUnit.SECONDS);
                    } else {
                        mPogoResponse.clear();
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

        private List<PayloadEntity> operateMessage(ResponseMessage message) {
            if (message.isResponse() == true) {
                mOperators.setAck(message.getmessageId());
            } else {
                //  Message is request format
                if (message.isAction() == true) {
                    if (message.isTake() == true) {
                        mOperators.setTake();
                    } else if (message.isResolve() == true) {
                        mOperators.setResolve();
                    } else {
                        Log.e(TAG, "message is ignored");
                    }
                }
            }
            //  Remove to be ready
            mOperators.removeReadyToBe();
            return message.getPayloadList();
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

                            List<PayloadEntity> payloadList = null;

                            synchronized (MESSAGE_INSTANCE_LOCK) {
                                payloadList = operateMessage(message);
                            }

                            if (payloadList != null) {
                                dispatchPayload(payloadList);
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

