package com.longingfuture.parval.common;

/**
 * 单例对象的容器
 * @author chenglei
 *
 * @param <T> 对象类型
 */
public abstract class SingletonHolder<T> {

    private volatile T value;
    private final Object monitor = new Object();

    public void clear() {
        this.value = null;
    }

    public T getValue() {
        return this.value;
    }

    /**
     * 获取对象，保证对象只被创建一次
     * @return
     */
    public T getOrCreateIfNecessary() {
        T currentValue = this.value;
        if (currentValue != null) {
            return currentValue;
        }

        synchronized (this.monitor) {

            currentValue = this.value;

            if (currentValue != null) {
                return currentValue;
            }

            return this.value = this.createObject();

        }

    }

    /**
     * 对象的创建函数，保证只被调用一次
     * @return
     */
    protected abstract T createObject();

}
