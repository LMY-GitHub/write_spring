package com.boy.spring.formework.beans;

public class BoyBeanWrapper {
    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public BoyBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    /**
     * 返回代理以后的Class
     * 可能会是 $Proxy0
     *
     * @return 代理以后的class
     */
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
