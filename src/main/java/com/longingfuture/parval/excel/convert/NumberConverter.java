package com.longingfuture.parval.excel.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter extends AbstractConverter {

    @Override
    public Object convert2type(Class<?> targetType, Object value) {

        Class<?> sourceType = value.getClass();
        // Handle Number
        String stringValue = value.toString().trim();
        Number number = null;
        stringValue = this.format(stringValue);
        number = this.toNumber(sourceType, targetType, stringValue);

        return this.toNumber(sourceType, targetType, number);
    }

    private Number toNumber(Class<?> sourceType, Class<?> targetType, Number value) {
        // Correct Number type already
        if (targetType.equals(value.getClass())) {
            return value;
        }
        // Byte
        if (targetType.equals(Byte.class)) {
            long longValue = value.longValue();
            if (longValue > Byte.MAX_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too large for " + targetType);
            }
            if (longValue < Byte.MIN_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too small " + targetType);
            }
            return new Byte(value.byteValue());
        }
        // Short
        if (targetType.equals(Short.class)) {
            long longValue = value.longValue();
            if (longValue > Short.MAX_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too large for " + targetType);
            }
            if (longValue < Short.MIN_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too small " + targetType);
            }
            return new Short(value.shortValue());
        }
        // Integer
        if (targetType.equals(Integer.class)) {
            long longValue = value.longValue();
            if (longValue > Integer.MAX_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too large for " + targetType);
            }
            if (longValue < Integer.MIN_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too small " + targetType);
            }
            return new Integer(value.intValue());
        }
        // Long
        if (targetType.equals(Long.class)) {
            return new Long(value.longValue());
        }
        // Float
        if (targetType.equals(Float.class)) {
            if (value.doubleValue() > Float.MAX_VALUE) {
                throw new ConvertException(sourceType + " value '" + value + "' is too large for " + targetType);
            }
            return new Float(value.floatValue());
        }
        // Double
        if (targetType.equals(Double.class)) {
            return new Double(value.doubleValue());
        }
        // BigDecimal
        if (targetType.equals(BigDecimal.class)) {
            if (value instanceof Float || value instanceof Double) {
                return new BigDecimal(value.toString());
            } else if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger) value);
            } else {
                return BigDecimal.valueOf(value.longValue());
            }
        }
        // BigInteger
        if (targetType.equals(BigInteger.class)) {
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).toBigInteger();
            } else {
                return BigInteger.valueOf(value.longValue());
            }
        }
        throw new ConvertException(this.getClass() + " cannot handle conversion from '" + sourceType + "' to '"
                + targetType + "'");
    }

    private Number toNumber(Class<?> sourceType, Class<?> targetType, String value) {
        // Byte
        if (targetType.equals(Byte.class)) {
            return Byte.valueOf(value);
        }
        // Short
        if (targetType.equals(Short.class)) {
            return Short.valueOf(value);
        }
        // Integer
        if (targetType.equals(Integer.class)) {
            return Integer.valueOf(value);
        }
        // Long
        if (targetType.equals(Long.class)) {
            return Long.valueOf(value);
        }
        // Float
        if (targetType.equals(Float.class)) {
            return Float.valueOf(value);
        }
        // Double
        if (targetType.equals(Double.class)) {
            return Double.valueOf(value);
        }
        // BigDecimal
        if (targetType.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        // BigInteger
        if (targetType.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        throw new ConvertException(this.getClass() + " cannot handle conversion from '" + sourceType + "' to '"
                + targetType + "'");
    }

    public String format(String stringValue) {
        return stringValue.replaceAll(this.regex, "");
    }

    private String regex = "\\.0+$";

}
