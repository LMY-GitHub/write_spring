package com.boy.spring.formework.context.support;

/**
 * IOC 容器实现的顶层设计
 */
public abstract class BoyAbstractApplicationContext {
    /**
     * 受保护的，只提供给子类重写
     *
     * @throws Exception 异常
     */
    public void refresh() throws Exception {
    }
}
