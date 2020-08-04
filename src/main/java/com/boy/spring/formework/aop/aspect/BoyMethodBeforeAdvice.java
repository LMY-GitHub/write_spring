package com.boy.spring.formework.aop.aspect;

import com.boy.spring.formework.aop.BoyAbstractAspectJAdvice;
import com.boy.spring.formework.aop.BoyAdvice;
import com.boy.spring.formework.aop.intercept.BoyMethodInterceptor;
import com.boy.spring.formework.aop.intercept.BoyMethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知具体实现
 */
public class BoyMethodBeforeAdvice extends BoyAbstractAspectJAdvice implements BoyAdvice, BoyMethodInterceptor {

    private BoyJoinPoint joinPoint;

    public BoyMethodBeforeAdvice(Method aspectMethod, Object target) {
        super(aspectMethod, target);
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        invokeAdviceMethod(this.joinPoint, null, null);
    }

    public Object invoke(BoyMethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
