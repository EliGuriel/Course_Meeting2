package org.example.transactionaldemo.exception;

// Resource already exists exception - for 409 errors
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}