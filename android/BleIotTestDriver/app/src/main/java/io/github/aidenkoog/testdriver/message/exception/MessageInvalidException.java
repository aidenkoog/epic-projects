package io.github.aidenkoog.testdriver.message.exception;

public class MessageInvalidException extends Exception {

    public MessageInvalidException(String message) {
        super(message);
    }

    public MessageInvalidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

