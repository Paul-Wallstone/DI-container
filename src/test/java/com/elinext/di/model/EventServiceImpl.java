package com.elinext.di.model;

import com.elinext.di.annotations.Inject;

public class EventServiceImpl implements EventService {
    private EventDAO eventDao;

    @Inject
    public EventServiceImpl(EventDAO eventDao) {
        this.eventDao = eventDao;
    }

    public EventServiceImpl() {
    }

    @Override
    public void read() {
        eventDao.read();
    }

    public EventDAO getEventDao() {
        return eventDao;
    }
}
