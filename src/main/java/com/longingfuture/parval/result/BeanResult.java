package com.longingfuture.parval.result;

import java.util.List;

public class BeanResult<T> {

    private List<BeanWrapper<T>> successList;
    private List<BeanWrapper<T>> failList;

    public List<BeanWrapper<T>> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<BeanWrapper<T>> successList) {
        this.successList = successList;
    }

    public List<BeanWrapper<T>> getFailList() {
        return failList;
    }

    public void setFailList(List<BeanWrapper<T>> failList) {
        this.failList = failList;
    }

}
