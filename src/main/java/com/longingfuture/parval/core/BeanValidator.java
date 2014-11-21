package com.longingfuture.parval.core;

import java.util.List;

import com.longingfuture.parval.result.BeanResult;
import com.longingfuture.parval.result.BeanWrapper;

public interface BeanValidator {

    public <T> BeanWrapper<T> validate(T t);

    public <T> BeanResult<T> validate(List<T> list);
}
