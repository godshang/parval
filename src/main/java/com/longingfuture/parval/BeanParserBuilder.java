package com.longingfuture.parval;

import com.longingfuture.parval.core.BeanParser;
import com.longingfuture.parval.core.impl.DefaultBeanParser;

public class BeanParserBuilder {

    public static BeanParser build() {
        return new DefaultBeanParser();
    }
}
