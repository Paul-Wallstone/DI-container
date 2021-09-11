package com.elinext.di.model;

import com.elinext.di.annotations.Inject;

public class EventServiceWrongParameterImpl implements EventService {
    private EventDAO eventDao;
    private String string;

    @Inject
    public EventServiceWrongParameterImpl(String string) {
        this.string = string;
    }

    public EventServiceWrongParameterImpl(EventDAO eventDao) {
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
