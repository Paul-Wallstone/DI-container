package com.elinext.di.model;


public class InMemoryEventDAOImpl implements EventDAO {

    String str;

    public InMemoryEventDAOImpl() {
        str = "Hello DI!";
    }

    @Override
    public void read() {
        System.out.println(str);
    }
}
