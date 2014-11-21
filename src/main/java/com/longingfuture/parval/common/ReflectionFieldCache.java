package com.longingfuture.parval.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.longingfuture.parval.util.ReflectionUtils;
import com.longingfuture.parval.util.ReflectionUtils.FieldCallback;

public class ReflectionFieldCache {

    private static final ConcurrentHashMap<Class<?>, FieldHolder> fieldCache = new ConcurrentHashMap<Class<?>, FieldHolder>();

    private static final ConcurrentHashMap<Class<?>, AllFieldHolder> allFieldCache = new ConcurrentHashMap<Class<?>, AllFieldHolder>();

    static class FieldHolder extends SingletonHolder<Field[]> {

        private Class<?> clazz;

        public FieldHolder(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        protected Field[] createObject() {
            return doGetDeclaredFields(clazz);
        }

    }

    static class AllFieldHolder extends SingletonHolder<Field[]> {

        private Class<?> clazz;

        public AllFieldHolder(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        protected Field[] createObject() {
            return doGetAllDeclaredFields(clazz);
        }

    }

    public static Field[] getDeclaredFields(Class<?> clazz) {
        FieldHolder holder = fieldCache.get(clazz);
        if (holder == null) {
            fieldCache.putIfAbsent(clazz, new FieldHolder(clazz));
            holder = fieldCache.get(clazz);
        }
        return holder.getOrCreateIfNecessary();
    }

    public static Field[] getAllDeclaredFields(Class<?> clazz) {
        AllFieldHolder holder = allFieldCache.get(clazz);
        if (holder == null) {
            allFieldCache.putIfAbsent(clazz, new AllFieldHolder(clazz));
            holder = allFieldCache.get(clazz);
        }
        return holder.getOrCreateIfNecessary();
    }

    private static Field[] doGetDeclaredFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        final List list = new ArrayList(fields.length);
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            if (!field.isSynthetic()) {
                list.add(field);
            }
        }
        return (Field[]) list.toArray(new Field[list.size()]);
    }

    private static Field[] doGetAllDeclaredFields(Class<?> clazz) {
        final List list = new ArrayList(32);
        ReflectionUtils.doWithFields(clazz, new FieldCallback() {

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                if (!field.isSynthetic()) {
                    list.add(field);
                }
            }

        });
        return (Field[]) list.toArray(new Field[list.size()]);
    }
}
