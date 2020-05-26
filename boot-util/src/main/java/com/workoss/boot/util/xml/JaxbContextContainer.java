package com.workoss.boot.util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class JaxbContextContainer {
    private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<>(64);


    public Marshaller createMarshaller(Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        return jaxbContext.createMarshaller();
    }

    public Unmarshaller createUnmarshaller(Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        return jaxbContext.createUnmarshaller();
    }

    private JAXBContext getJaxbContext(Class<?> clazz)  {
        return this.jaxbContexts.computeIfAbsent(clazz, key -> {
            try {
                return JAXBContext.newInstance(clazz);
            }
            catch (JAXBException ex) {
                throw new RuntimeException(
                        "Could not create JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        });
    }
}