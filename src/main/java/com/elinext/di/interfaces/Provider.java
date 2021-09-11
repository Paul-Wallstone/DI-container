package com.elinext.di.interfaces;

public interface Provider<T> {
    T getInstance() throws Exception;
}
