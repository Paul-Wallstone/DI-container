package com.elinext.di.model;

import com.elinext.di.annotations.Inject;

public class EventServiceTestTwoConstructorImpl implements EventService {
    private EventDAO eventDao;
    String someString;

    @Inject
    public EventServiceTestTwoConstructorImpl(EventDAO eventDao) {
        this.eventDao = eventDao;
    }

    @Inject
    public EventServiceTestTwoConstructorImpl(String someString) {
        this.someString = someString;
    }

    @Override
    public void read() {
        eventDao.read();
    }

    @Override
    public EventDAO getEventDao() {
        return eventDao;
    }
}
