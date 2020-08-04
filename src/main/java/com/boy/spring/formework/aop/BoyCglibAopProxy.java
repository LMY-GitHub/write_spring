package com.boy.spring.formework.aop;

import com.boy.spring.formework.aop.support.BoyAdvisedSupport;

/**
 * 使用CGlib API 生成代理类
 */
public class BoyCglibAopProxy implements BoyAopProxy {
    private BoyAdvisedSupport config;

    public BoyCglibAopProxy(BoyAdvisedSupport config) {
        this.config = config;
    }

    public Object getProxy() {
        return null;
    }

    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
