package com.boy.spring.formework.aop.aspect;

import com.boy.spring.formework.aop.BoyAbstractAspectJAdvice;
import com.boy.spring.formework.aop.BoyAdvice;
import com.boy.spring.formework.aop.intercept.BoyMethodInterceptor;
import com.boy.spring.formework.aop.intercept.BoyMethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知具体实现
 */
public class BoyAfterReturningAdvice extends BoyAbstractAspectJAdvice implements BoyAdvice, BoyMethodInterceptor {

    private BoyJoinPoint joinPoint;

    public BoyAfterReturningAdvice(Method aspectMethod, Object target) {
        super(aspectMethod, target);
    }

    public Object invoke(BoyMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    public void afterReturning(Object returnValues, Method method, Object[] args, Object target) throws Throwable {
        invokeAdviceMethod(joinPoint, returnValues, null);
    }

}
