package com.longingfuture.parval;

import com.longingfuture.parval.core.BeanValidator;
import com.longingfuture.parval.core.impl.DefaultBeanValidator;

public class BeanValidatorBuilder {

    public static BeanValidator build() {
        return new DefaultBeanValidator();
    }

}
