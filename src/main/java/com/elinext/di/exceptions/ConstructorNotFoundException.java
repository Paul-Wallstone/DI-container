package com.elinext.di.exceptions;

public class ConstructorNotFoundException extends Exception{
    public ConstructorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConstructorNotFoundException(String message){
        super(message);
    }
}
