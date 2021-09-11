package com.elinext.di.model;

public class EventServiceTestNoConstructorImpl implements EventService {
    private EventDAO eventDao;

    private EventServiceTestNoConstructorImpl(EventDAO eventDao) {
        this.eventDao = eventDao;
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
