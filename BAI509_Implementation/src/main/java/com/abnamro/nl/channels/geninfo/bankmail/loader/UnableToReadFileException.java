package com.abnamro.nl.channels.geninfo.bankmail.loader;

public class UnableToReadFileException extends Exception {
    public UnableToReadFileException(String message) {
        super(message);
    }

    public UnableToReadFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
