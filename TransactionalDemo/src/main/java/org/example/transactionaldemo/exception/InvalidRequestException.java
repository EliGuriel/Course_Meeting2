package org.example.transactionaldemo.exception;

// Invalid request exception - for general 400 errors
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}