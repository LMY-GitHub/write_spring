package com.boy.spring.formework.aop;

/**
 * 代理工厂的顶层接口，提供获取代理对象的顶层入口
 * 默认就是jdk动态代理
 */
public interface BoyAopProxy {
    //  获得一个动态代理
    Object getProxy();

    //  通过自定义类加载器获得一个代理对象
    Object getProxy(ClassLoader classLoader);
}
