package com.longingfuture.parval.excel.convert;

public class BooleanConverter extends AbstractConverter {

    private static String[] trueStrings = { "TRUE", "YES", "Y", "ON", "1", "是", "返回" };

    private static String[] falseStrings = { "FALSE", "NO", "N", "OFF", "0", "否", "不返回" };

    public Object convert2type(Class<?> type, Object value) {
        String stringValue = value.toString().toUpperCase();

        for (int i = 0; i < trueStrings.length; ++i) {
            if (trueStrings[i].equals(stringValue)) {
                return Boolean.TRUE;
            }
        }

        for (int i = 0; i < falseStrings.length; ++i) {
            if (falseStrings[i].equals(stringValue)) {
                return Boolean.FALSE;
            }
        }

        return null;
    }

}
