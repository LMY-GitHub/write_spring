package com.boy.spring.formework.beans.config;

public class BoyBeanDefinition {
    //  原生 bean 的全类名
    private String beanClassName;

    //  标记是否延时加载
    private boolean lazyInit = false;

    //  保存 beanName， 在IOC容器中存储的 key
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
