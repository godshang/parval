package com.longingfuture.parval.excel.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ConverterHolder {

    private static ConverterHolder instance = new ConverterHolder();

    private Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();

    private ConverterHolder() {
        this.register();
    }

    public static ConverterHolder getInstance() {
        return instance;
    }

    private void register() {

        this.register(Boolean.class, new BooleanConverter());
        this.register(BigDecimal.class, new NumberConverter());
        this.register(BigInteger.class, new NumberConverter());
        this.register(Byte.class, new NumberConverter());
        this.register(Double.class, new NumberConverter());
        this.register(Float.class, new NumberConverter());
        this.register(Integer.class, new NumberConverter());
        this.register(Long.class, new NumberConverter());
        this.register(Short.class, new NumberConverter());

        this.register(Byte.TYPE, new NumberConverter());
        this.register(Double.TYPE, new NumberConverter());
        this.register(Float.TYPE, new NumberConverter());
        this.register(Integer.TYPE, new NumberConverter());
        this.register(Long.TYPE, new NumberConverter());
        this.register(Short.TYPE, new NumberConverter());
        this.register(Boolean.TYPE, new BooleanConverter());
    }

    private void register(Class<?> clazz, Converter converter) {

        this.register(new ConvertFacade(converter), clazz);

    }

    public void register(Converter converter, Class<?> clazz) {

        synchronized (ConverterHolder.class) {
            this.converters.put(clazz, converter);
        }

    }

    public Converter lookup(Class<?> clazz) {

        return (this.converters.get(clazz));

    }

}
