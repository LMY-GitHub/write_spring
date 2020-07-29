package com.boy.spring.formework.context;

/**
 * 通过解耦方式获得IoC容器的顶层设计
 * 后面将通过一个监听器去扫描所有的类，只有实现了此接口
 * 将自动调用 setApplicationContext() 方法
 */
public interface BoyApplicationContextAware {
    void setApplicationContext(BoyApplicationContext applicationContext);
}
