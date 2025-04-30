package org.example.stage1.exception;

/**
 * Exception thrown when a resource does not exist in the system.
 * For example, when trying to retrieve a student that does not exist.
 */
public class NotExists extends RuntimeException {
    public NotExists(String message) {
        super(message);
    }
}
