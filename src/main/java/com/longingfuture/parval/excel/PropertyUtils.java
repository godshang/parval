package com.longingfuture.parval.excel;

import java.lang.reflect.Field;

import com.longingfuture.parval.annotation.ExcelTitle;
import com.longingfuture.parval.common.ReflectionFieldCache;
import com.longingfuture.parval.excel.convert.ConverterHolder;

public final class PropertyUtils {

    public static <T> void setProperty(T bean, String titleValue, Object value) throws IllegalAccessException {
        if (null != value) {
            Field[] fields = getAllDeclaredFields(bean.getClass());
            for (Field field : fields) {
                ExcelTitle title = field.getAnnotation(ExcelTitle.class);
                if (title != null && contains(title.names(), titleValue)) {
                    try {
                        field.set(bean, value);
                    } catch (IllegalArgumentException e) {
                        Class<?> clazz = field.getType();
                        Object obj = ConverterHolder.getInstance().lookup(clazz).convert(clazz, value);
                        field.set(bean, obj);
                    }
                }
            }
        }
    }

    protected static <T> Field[] getAllDeclaredFields(Class<?> clazz) {
        return ReflectionFieldCache.getAllDeclaredFields(clazz);
    }

    private static boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
