package com.elinext.di;

import com.elinext.di.annotations.Inject;
import com.elinext.di.exceptions.BindingNotFoundException;
import com.elinext.di.exceptions.ConstructorNotFoundException;
import com.elinext.di.exceptions.TooManyConstructorsException;
import com.elinext.di.interfaces.Injector;
import com.elinext.di.interfaces.Provider;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class InjectorImpl implements Injector {

    private Map<Class, Object> singletonInstances = new HashMap<>();

    private Set<Class> singletonClasses = new HashSet<>();

    private Map<Class, Class> interfaceMap = new HashMap<>();


    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        if (interfaceMap.containsKey(type)) {
            Provider<T> provider = () -> {
                return getInstance(type);
            };
            return provider;
        }
        return null;
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        bindInterface(intf, impl);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        markAsSingleton(impl);
        bindInterface(intf, impl);
    }

    public <T> T getInstance(Class<T> requestedType) throws BindingNotFoundException, ConstructorNotFoundException, TooManyConstructorsException {
        return getInstance(requestedType, null);
    }

    private <T> T getInstance(Class<T> requestedType, Class<?> parent) throws BindingNotFoundException, ConstructorNotFoundException, TooManyConstructorsException {
        Class<T> type = requestedType;
        if (requestedType.isInterface()) {
            if (interfaceMap.containsKey(requestedType)) {
                type = interfaceMap.get(requestedType);
            } else {
                throw new BindingNotFoundException("There is not found binding.");
            }
        }
        if (singletonInstances.containsKey(type)) {
            return (T) singletonInstances.get(type);
        }
        if (!requestedType.isInterface()) {
            throw new BindingNotFoundException("There is not found binding.");
        }
        return createNewInstance(type, parent);
    }

    private <T> T createNewInstance(Class<T> type, Class<?> parent) throws ConstructorNotFoundException, BindingNotFoundException, TooManyConstructorsException {
        final Constructor<T> constructor = findConstructor(type);
        final Parameter[] parameters = constructor.getParameters();
        final List<Object> arguments = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            arguments.add(getInstance(parameters[i].getType(), type));
        }
        try {
            final T newInstance = constructor.newInstance(arguments.toArray());

            if (isSingleton(type)) {
                singletonInstances.put(type, newInstance);
            }

            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isSingleton(Class type) {
        return singletonClasses.contains(type);
    }


    private <T> void bindInterface(Class<T> interfaceType, Class<? extends T> implementationType) {
        if (interfaceType.isInterface()) {
            if (implementationType.isInterface()) {
                throw new IllegalArgumentException(
                        "The given type is an interface. Expecting the second argument is not an interface but an actual class");
            } else {
                interfaceMap.put(interfaceType, implementationType);
            }
        } else {
            throw new IllegalArgumentException(
                    "The given type is not an interface. Expecting the first argument to be an interface.");
        }
    }


    private <T> Constructor<T> findConstructor(Class<T> type) throws ConstructorNotFoundException, TooManyConstructorsException {
        final Constructor<?>[] constructors = type.getConstructors();

        if (constructors.length == 0) {
            throw new ConstructorNotFoundException(
                    "There is no any public constructor.");
        }

        if (constructors.length > 1) {

            final List<Constructor<?>> constructorsWithInject = Arrays
                    .stream(constructors)
                    .filter(c -> c.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());

            if (constructorsWithInject.isEmpty()) {
                throw new ConstructorNotFoundException(
                        "There is no any public constructor.");
            }

            if (constructorsWithInject.size() != 1) {
                throw new TooManyConstructorsException(
                        "More than one public constructor.");
            }

            return (Constructor<T>) constructorsWithInject.get(0);
        } else {
            return (Constructor<T>) constructors[0];
        }
    }

    private void markAsSingleton(Class type) {
        if (type.isInterface()) {
            throw new IllegalArgumentException(
                    "The given type is an interface. Expecting param is an actual class");
        }
        singletonClasses.add(type);
    }

}
