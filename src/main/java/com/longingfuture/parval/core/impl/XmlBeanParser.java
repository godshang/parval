package com.longingfuture.parval.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.longingfuture.parval.common.ErrorConstant;
import com.longingfuture.parval.common.SingletonHolder;
import com.longingfuture.parval.core.BeanParser;
import com.longingfuture.parval.exception.BeanParseException;

public class XmlBeanParser implements BeanParser {

    private static final Log log = LogFactory.getLog(XmlBeanParser.class);

    private ConcurrentHashMap<Class<?>, JAXBContextHolder> jaxbContextMap;

    class JAXBContextHolder extends SingletonHolder<JAXBContext> {

        private Class<?> clazz;

        public JAXBContextHolder(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        protected JAXBContext createObject() {
            return createJAXBContext(clazz);
        }

    }

    public XmlBeanParser() {
        jaxbContextMap = new ConcurrentHashMap<Class<?>, JAXBContextHolder>();
    }

    @Override
    public <T> List<T> parse(InputStream in, Class<T> clazz) throws BeanParseException {
        JAXBContext jaxbContext = getJAXBContext(clazz);
        if (jaxbContext == null) {
            throw new IllegalArgumentException("JAXBContext is null!");
        }

        Unmarshaller unmarshaller = null;
        List<T> list = new ArrayList<T>();
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
            list.add((T) unmarshaller.unmarshal(in));
        } catch (JAXBException e) {
            throw new BeanParseException(ErrorConstant.FORMAT_ERROR, e);
        }
        return list;
    }

    @Override
    public <T> List<T> parse(File file, Class<T> clazz) throws BeanParseException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BeanParseException(ErrorConstant.READ_DATA_ERROR, e);
        }
        return parse(in, clazz);
    }

    protected JAXBContext getJAXBContext(Class<?> clazz) {
        JAXBContextHolder jaxbContextHolder = this.jaxbContextMap.get(clazz);
        if (jaxbContextHolder == null) {
            this.jaxbContextMap.putIfAbsent(clazz, new JAXBContextHolder(clazz));
            jaxbContextHolder = this.jaxbContextMap.get(clazz);
        }
        return jaxbContextHolder.getOrCreateIfNecessary();
    }

    protected JAXBContext createJAXBContext(Class<?> clazz) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            log.error("create JAXBContext error!", e);
        }
        return context;
    }

}
