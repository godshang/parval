package com.longingfuture.parval.core.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import com.longingfuture.parval.common.ErrorConstant;
import com.longingfuture.parval.common.SingletonHolder;
import com.longingfuture.parval.core.BeanParser;
import com.longingfuture.parval.exception.BeanParseException;
import com.longingfuture.parval.util.ClassUtils;
import com.longingfuture.parval.util.FileCopyUtils;
import com.longingfuture.parval.util.JsonUtils;
import com.longingfuture.parval.util.JsonUtils.JsonType;

public class JsonBeanParser implements BeanParser {

    private final ObjectMapper mapper;
    private final TypeFactory typeFactory;
    private ConcurrentHashMap<Class<?>, JavaTypeHolder> typeMap;

    class JavaTypeHolder extends SingletonHolder<JavaType> {

        public Class<?> clazz;

        public JavaTypeHolder(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        protected JavaType createObject() {
            return createTypeReference(clazz);
        }

    }

    public JsonBeanParser() {
        mapper = new ObjectMapper();
        typeFactory = TypeFactory.defaultInstance();
        typeMap = new ConcurrentHashMap<Class<?>, JavaTypeHolder>();
    }

    @Override
    public <T> List<T> parse(InputStream in, Class<T> clazz) throws BeanParseException {
        byte[] src = null;
        try {
            src = FileCopyUtils.copyToByteArray(in);
        } catch (IOException e) {
            throw new BeanParseException(ErrorConstant.READ_DATA_ERROR, e);
        }
        return readValue(src, clazz);
    }

    @Override
    public <T> List<T> parse(File file, Class<T> clazz) throws BeanParseException {
        byte[] src = null;
        try {
            src = FileCopyUtils.copyToByteArray(file);
        } catch (IOException e) {
            throw new BeanParseException(ErrorConstant.READ_DATA_ERROR, e);
        }
        return readValue(src, clazz);
    }

    private <T> List<T> readValue(byte[] src, Class<T> clazz) throws BeanParseException {
        JsonType jsonType = JsonUtils.getJsonType(src);
        if (jsonType == JsonType.UNKNOWN) {
            throw new IllegalArgumentException("json type error!");
        }

        List<T> list = null;
        try {
            if (jsonType == JsonType.ARRAY) {
                JavaType type = getJavaType(clazz);
                list = mapper.readValue(src, type);
            } else if (jsonType == JsonType.OBJECT) {
                list = new ArrayList<T>(1);
                list.add(mapper.readValue(src, clazz));
            }
        } catch (Exception e) {
            if (ClassUtils.isAssignableValue(JsonParseException.class, e)
                    || ClassUtils.isAssignableValue(JsonMappingException.class, e)
                    || ClassUtils.isAssignableValue(IOException.class, e)) {
                throw new BeanParseException(ErrorConstant.FORMAT_ERROR, e);
            }
            throw new BeanParseException(ErrorConstant.UNKNOW_ERROR, e);
        }
        return list;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        JavaTypeHolder holder = typeMap.get(clazz);
        if (holder == null) {
            this.typeMap.putIfAbsent(clazz, new JavaTypeHolder(clazz));
            holder = this.typeMap.get(clazz);
        }
        return holder.getOrCreateIfNecessary();
    }

    protected JavaType createTypeReference(Class<?> clazz) {
        return typeFactory.constructCollectionType(List.class, clazz);
    }
}
