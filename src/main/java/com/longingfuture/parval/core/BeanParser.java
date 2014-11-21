package com.longingfuture.parval.core;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.longingfuture.parval.exception.BeanParseException;

public interface BeanParser {

    public <T> List<T> parse(InputStream in, Class<T> clazz) throws BeanParseException;

    public <T> List<T> parse(File file, Class<T> clazz) throws BeanParseException;

}
