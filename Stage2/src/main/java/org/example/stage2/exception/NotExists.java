package org.example.stage2.exception;

public class NotExists extends RuntimeException {
    public NotExists(String message) {
        super(message);
    }
}
