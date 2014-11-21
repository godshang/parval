package com.longingfuture.parval.result;

import java.util.List;

public class BeanWrapper<T> {

    private T bean;

    private List<String> errorMsgs;

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    public List<String> getErrorMsgs() {
        return errorMsgs;
    }

    public void setErrorMsgs(List<String> errorMsgs) {
        this.errorMsgs = errorMsgs;
    }

}
