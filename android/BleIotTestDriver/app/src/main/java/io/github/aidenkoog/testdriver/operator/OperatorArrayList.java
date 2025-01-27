package io.github.aidenkoog.testdriver.operator;

import android.util.Log;

import io.github.aidenkoog.testdriver.message.MessageOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface OperatorList extends List<MessageOperator> {

    public boolean hasPriority();

    public MessageOperator getPriority();

    public OperatorList getOperators(byte[] messageId);

    public OperatorList getOperatorsReadyToBeSent();

    public void setSent(byte[] messageId);

    public void setAck(byte[] messageId);

    public void setTake();

    public void setResolve();

    public void removeReadyToBe();

    public boolean addOp(MessageOperator operator);

    public boolean removeOp(Object object);

    public void removeAll();
}

public class OperatorArrayList extends ArrayList<MessageOperator> implements OperatorList {

    private static final String TAG = OperatorArrayList.class.getSimpleName();

    public boolean hasPriority() {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isPriority()) {
                return true;
            }
        }
        return false;
    }

    public MessageOperator getPriority() {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isPriority()) {
                return operator;
            }
        }
        return null;
    }

    public OperatorList getOperators(byte[] messageId) {
        OperatorList operators = new OperatorArrayList();
        for (MessageOperator operator : OperatorArrayList.this) {
            if (Arrays.equals(operator.getMessageId(), messageId)) {
                operators.add(operator);
            }
        }
        return operators;
    }

    public OperatorList getOperatorsReadyToBeSent() {
        OperatorList operators = new OperatorArrayList();
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isReadyToBeSent() == true) {
                operators.add(operator);
            }
        }
        return operators;
    }

    public void setSent(byte[] messageId) {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (Arrays.equals(operator.getMessageId(), messageId)) {
                operator.setSent();
            }
        }
    }

    public void setAck(byte[] messageId) {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (Arrays.equals(operator.getMessageId(), messageId)) {
                operator.setAck();
            }
        }
    }

    public void setTake() {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isPriority()) {
                operator.setTake();
            }
        }
    }

    public void setResolve() {
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isPriority()) {
                operator.setResolve();
            }
        }
    }

    public void removeReadyToBe() {
        OperatorList operators = new OperatorArrayList();
        for (MessageOperator operator : OperatorArrayList.this) {
            if (operator.isReadyToBeRemoved() == true) {
                operators.add(operator);
            }
        }
        for (MessageOperator operator : operators) {
            removeOp((Object)operator);
        }
    }

    public boolean addOp(MessageOperator operator) {
        boolean ret = false;
        if (operator.isPriority() == true) {
            if (hasPriority() == true) {
                Log.e(TAG, "Block addOp");
                //  Must be not added
                return false;
            }
        }
        ret = add(operator);
        operator.added();
        return ret;
    }

    public boolean removeOp(Object object) {
        boolean ret = remove(object);
        ((MessageOperator)object).removed();
        return ret;
    }

    public void removeAll() {
        OperatorList operators = new OperatorArrayList();
        for (MessageOperator operator : OperatorArrayList.this) {
            operators.add(operator);
        }
        for (MessageOperator operator : operators) {
            removeOp((Object)operator);
        }
    }
}
