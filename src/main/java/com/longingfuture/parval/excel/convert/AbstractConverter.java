package com.longingfuture.parval.excel.convert;

public abstract class AbstractConverter implements Converter {

    public Object convert(Class<?> type, Object value) {

        Class<?> sourceType = value == null ? null : value.getClass();
        Class<?> targetType = this.primitive(type);
        try {
            if (targetType.equals(String.class)) {
                // Convert --> String
                return value.toString();
            } else if (targetType.equals(sourceType)) {
                // No conversion necessary
                return value;
            } else {
                // Convert --> Type
                Object result = this.convert2type(targetType, value);
                return result;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public abstract Object convert2type(Class<?> type, Object value);

    private Class<?> primitive(Class<?> type) {
        if (type == null || !type.isPrimitive()) {
            return type;
        }

        switch (Type.valueOf(type.toString().toUpperCase())) {
        case INT:
            return Integer.class;
        case DOUBLE:
            return Double.class;
        case LONG:
            return Long.class;
        case BOOLEAN:
            return Boolean.class;
        case FLOAT:
            return Float.class;
        case SHORT:
            return Short.class;
        case BYTE:
            return Byte.class;
        case CHAR:
            return Character.class;
        default:
            return type;
        }
    }

    private static enum Type {

        INT, LONG, BOOLEAN, BYTE, SHORT, CHAR, FLOAT, DOUBLE, STRING, BIGDECIMAL;

    }

}
