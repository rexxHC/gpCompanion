package com.gpcompanion.exceptions;

/// throws an exception if an error occurs while handling race data

public class RaceDataException extends Exception{
    public RaceDataException(String message) {
        super(message);
    }

    public RaceDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
