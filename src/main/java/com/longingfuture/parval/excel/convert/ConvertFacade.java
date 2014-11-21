package com.longingfuture.parval.excel.convert;

public class ConvertFacade implements Converter {

    private final Converter converter;

    public ConvertFacade(Converter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter is missing");
        }
        this.converter = converter;
    }

    public Object convert(Class<?> type, Object value) {
        return this.converter.convert(type, value);
    }

}
