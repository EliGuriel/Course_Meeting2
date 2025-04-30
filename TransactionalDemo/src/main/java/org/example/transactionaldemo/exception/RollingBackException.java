package org.example.transactionaldemo.exception;

public class RollingBackException extends IllegalArgumentException {
    public RollingBackException(String message) {
        super(message);
    }

    public RollingBackException(String message, Throwable cause) {
        super(message, cause);
    }
}
