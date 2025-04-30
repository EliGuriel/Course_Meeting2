package org.example.transactionaldemo.exception;

// ID mismatch exception - for 400 errors related to ID conflicts
public class IdMismatchException extends RuntimeException {
    public IdMismatchException(String message) {
        super(message);
    }
}