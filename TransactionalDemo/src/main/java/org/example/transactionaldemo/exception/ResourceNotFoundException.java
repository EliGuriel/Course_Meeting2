package org.example.transactionaldemo.exception;

// Resource not found exception - for 404 errors
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}