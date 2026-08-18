package com.gpcompanion.exceptions;

/// This class throws exceptions in case of failures during the authentication process

public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
