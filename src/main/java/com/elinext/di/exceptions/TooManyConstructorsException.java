package com.elinext.di.exceptions;

public class TooManyConstructorsException extends Exception {
    public TooManyConstructorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyConstructorsException(String message) {
        super(message);
    }
}
