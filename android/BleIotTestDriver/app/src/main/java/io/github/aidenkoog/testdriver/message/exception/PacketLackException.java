package io.github.aidenkoog.testdriver.message.exception;

public class PacketLackException extends Exception {

    public PacketLackException(String message) {
        super(message);
    }

    public PacketLackException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

