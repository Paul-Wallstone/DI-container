package com.elinext.di;


import com.elinext.di.exceptions.BindingNotFoundException;
import com.elinext.di.exceptions.ConstructorNotFoundException;
import com.elinext.di.exceptions.TooManyConstructorsException;
import com.elinext.di.interfaces.Injector;
import com.elinext.di.interfaces.Provider;
import com.elinext.di.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InjectorImplTest {


    @Test
    @DisplayName("Test - bind two class and return object ")
    void testExistingBinding() throws Exception {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        injector.bind(EventDAO.class, InMemoryEventDAOImpl.class); //добавляем в инжектор реализацию интерфейса
        Provider<EventDAO> daoProvider = injector.getProvider(EventDAO.class); //получаем инстанс класса из инжектора
        assertNotNull(daoProvider);
        assertNotNull(daoProvider.getInstance());
        assertSame(InMemoryEventDAOImpl.class, daoProvider.getInstance().getClass());
    }

    @Test
    @DisplayName("Test - bind two class and return singleton object ")
    void testSingletonBinding() throws Exception {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        Provider<EventDAO> daoProvider = injector.getProvider(EventDAO.class);
        injector.bind(EventService.class, EventServiceImpl.class);
        Provider<EventService> serviceProvider = injector.getProvider(EventService.class);
        assertSame(serviceProvider.getInstance().getEventDao(), daoProvider.getInstance());
    }


    @Test
    @DisplayName("Test - Return TooManyConstructorException ")
    void testTooConstructorsExceptions() throws Exception {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        injector.bind(EventService.class, EventServiceTestTwoConstructorImpl.class);
        Provider<EventService> serviceProvider = injector.getProvider(EventService.class);
        Throwable exception = assertThrows(TooManyConstructorsException.class, () ->
                serviceProvider.getInstance());
        assertEquals("More than one public constructor.", exception.getMessage());
    }

    @Test
    @DisplayName("Test - Return ConstructorNotFoundException ")
    void testConstructorNotFoundException() throws Exception {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        injector.bind(EventService.class, EventServiceTestNoConstructorImpl.class);
        Provider<EventService> serviceProvider = injector.getProvider(EventService.class);
        Throwable exception = assertThrows(ConstructorNotFoundException.class, () ->
                serviceProvider.getInstance());
        assertEquals("There is no any public constructor.", exception.getMessage());
    }

    @Test
    @DisplayName("Test - Return null provider if binding not found")
    void testBindingNotFound() throws Exception {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        Provider<EventService> serviceProvider = injector.getProvider(EventService.class);
        assertEquals(null, serviceProvider);
    }

    @Test
    @DisplayName("Test - Return BindingNotFoundException")
    void testBindingNotFoundException() throws Exception {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        injector.bind(EventService.class, EventServiceWrongParameterImpl.class);
        Provider<EventService> serviceProvider = injector.getProvider(EventService.class);
        Throwable exception = assertThrows(BindingNotFoundException.class, () ->
                serviceProvider.getInstance().getEventDao());
        assertEquals("There is not found binding.", exception.getMessage());
    }

}
