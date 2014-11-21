package com.longingfuture.parval.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String date_format = "yyyy-MM-dd";

    private static final String time_format = "yyyy-MM-dd HH:mm:ss";

    @SuppressWarnings("rawtypes")
    private static ThreadLocal date_threadlocal = new ThreadLocal() {

        @Override
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(date_format);
        }
    };

    @SuppressWarnings("rawtypes")
    private static ThreadLocal time_threadlocal = new ThreadLocal() {

        @Override
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(time_format);
        }
    };

    public static DateFormat getDateFormat() {
        return (DateFormat) date_threadlocal.get();
    }

    public static Date parse(String str) throws ParseException {
        return getDateFormat().parse(str);
    }
}
