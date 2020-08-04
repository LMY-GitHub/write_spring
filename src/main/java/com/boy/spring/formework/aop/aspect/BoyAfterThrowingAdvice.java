package com.boy.spring.formework.aop.aspect;

import com.boy.spring.formework.aop.BoyAbstractAspectJAdvice;
import com.boy.spring.formework.aop.BoyAdvice;
import com.boy.spring.formework.aop.intercept.BoyMethodInterceptor;
import com.boy.spring.formework.aop.intercept.BoyMethodInvocation;

import java.lang.reflect.Method;

/**
 * 异常通知具体实现
 */
public class BoyAfterThrowingAdvice extends BoyAbstractAspectJAdvice implements BoyAdvice, BoyMethodInterceptor {

    private String throwingName;
    private BoyMethodInterceptor mi;

    public BoyAfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowingName(String name) {
        this.throwingName = name;
    }

    public Object invoke(BoyMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable ex) {
            invokeAdviceMethod(mi, null, ex.getCause());
            throw ex;
        }
    }
}
