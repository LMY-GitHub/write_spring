package com.boy.spring.formework.beans.config;

public class BoyBeanPostProcessor {

    //  为bean的初始化之前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    //  为bean的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
