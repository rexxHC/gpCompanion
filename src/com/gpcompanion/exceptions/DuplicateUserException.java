package com.gpcompanion.exceptions;

/// throws an exception in case a user tries to register a duplicate username

public class DuplicateUserException extends Exception{
    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
