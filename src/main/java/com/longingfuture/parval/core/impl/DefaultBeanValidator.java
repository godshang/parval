package com.longingfuture.parval.core.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.longingfuture.parval.annotation.NestedField;
import com.longingfuture.parval.common.ReflectionFieldCache;
import com.longingfuture.parval.core.BeanValidator;
import com.longingfuture.parval.result.BeanResult;
import com.longingfuture.parval.result.BeanWrapper;
import com.longingfuture.parval.util.ClassUtils;

public class DefaultBeanValidator implements BeanValidator {

    private static final Log log = LogFactory.getLog(DefaultBeanValidator.class);

    private Validator validator;

    public DefaultBeanValidator() {
        this.validator = new Validator();
    }

    @Override
    public <T> BeanWrapper<T> validate(T t) {
        BeanWrapper<T> beanWrapper = new BeanWrapper<T>();
        beanWrapper.setBean(t);

        List<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
        doValidate(t, violations);

        if (violations != null && !violations.isEmpty()) {
            List<String> errorMsgs = new ArrayList<String>(violations.size());
            for (ConstraintViolation violation : violations) {
                errorMsgs.add(violation.getMessage());
            }
            beanWrapper.setErrorMsgs(errorMsgs);
        }
        return beanWrapper;
    }

    @Override
    public <T> BeanResult<T> validate(List<T> list) {
        BeanResult<T> beanResult = new BeanResult<T>();
        List<BeanWrapper<T>> successList = new ArrayList<BeanWrapper<T>>();
        List<BeanWrapper<T>> failList = new ArrayList<BeanWrapper<T>>();

        for (T t : list) {
            BeanWrapper<T> beanWrapper = validate(t);
            List<String> errorMsgs = beanWrapper.getErrorMsgs();
            if (errorMsgs != null && !errorMsgs.isEmpty()) {
                failList.add(beanWrapper);
            } else {
                successList.add(beanWrapper);
            }
        }

        beanResult.setSuccessList(successList);
        beanResult.setFailList(failList);
        return beanResult;
    }

    protected void doValidate(Object obj, List<ConstraintViolation> violations) {
        violations.addAll(validator.validate(obj));

        Field[] fields = getFields(obj);
        for (Field field : fields) {
            NestedField nested = field.getAnnotation(NestedField.class);
            if (nested == null) {
                continue;
            }
            Class<?> fieldClass = field.getType();
            try {
                if (ClassUtils.isPrimitiveWrapper(fieldClass) || ClassUtils.isAssignable(String.class, fieldClass)) {
                    continue;
                } else if (ClassUtils.isAssignable(Collection.class, fieldClass)) {
                    Collection collection = (Collection) field.get(obj);
                    if (collection != null && collection.size() > 0) {
                        for (Object item : collection) {
                            doValidate(item, violations);
                        }
                    }
                } else if (fieldClass.isArray()) {
                    Object[] array = (Object[]) field.get(obj);
                    if (array != null && array.length > 0) {
                        for (Object item : array) {
                            doValidate(item, violations);
                        }
                    }
                } else {
                    Object value = field.get(obj);
                    if (value != null) {
                        doValidate(value, violations);
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected <T> Field[] getFields(T t) {
        Class<?> clazz = t.getClass();
        return ReflectionFieldCache.getDeclaredFields(clazz);
    }

}
