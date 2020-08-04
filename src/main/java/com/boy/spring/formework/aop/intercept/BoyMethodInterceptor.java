package com.boy.spring.formework.aop.intercept;

/**
 * 方法连接器顶层接口
 */
public interface BoyMethodInterceptor {
    Object invoke(BoyMethodInvocation mi) throws Throwable;
}
