package io.github.aidenkoog.testdriver.message.exception;

public class PacketInvalidException extends Exception {

    public PacketInvalidException(String message) {
        super(message);
    }

    public PacketInvalidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

