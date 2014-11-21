package com.longingfuture.parval.core.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longingfuture.parval.annotation.Source;
import com.longingfuture.parval.annotation.SourceType;
import com.longingfuture.parval.core.BeanParser;
import com.longingfuture.parval.exception.BeanParseException;

public class DefaultBeanParser implements BeanParser {

    private final Map<SourceType, BeanParser> parserMap = new HashMap<SourceType, BeanParser>();

    public DefaultBeanParser() {
        parserMap.put(SourceType.JSON, new JsonBeanParser());
        parserMap.put(SourceType.XML, new XmlBeanParser());
        parserMap.put(SourceType.EXCEL, new ExcelBeanParser());
    }

    @Override
    public <T> List<T> parse(InputStream in, Class<T> clazz) throws BeanParseException {
        Source source = getSource(clazz);

        BeanParser parser = getParser(source.value());

        return parser.parse(in, clazz);
    }

    @Override
    public <T> List<T> parse(File file, Class<T> clazz) throws BeanParseException {
        Source source = getSource(clazz);

        BeanParser parser = getParser(source.value());

        return parser.parse(file, clazz);
    }

    private Source getSource(Class<?> clazz) {
        Source source = clazz.getAnnotation(Source.class);
        if (source == null) {
            throw new IllegalArgumentException("Class '" + clazz + "' must have 'Source' annotation!");
        }
        return source;
    }

    private BeanParser getParser(SourceType sourceType) {
        BeanParser parser = parserMap.get(sourceType);
        if (parser == null) {
            throw new IllegalArgumentException("Bean parser for source type " + sourceType + " is not defined!");
        }
        return parser;
    }
}
