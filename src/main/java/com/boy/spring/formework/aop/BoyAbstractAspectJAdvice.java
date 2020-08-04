package com.boy.spring.formework.aop;

import com.boy.spring.formework.aop.aspect.BoyJoinPoint;

import java.lang.reflect.Method;

public abstract class BoyAbstractAspectJAdvice implements BoyAdvice {
    private Method aspectMethod;

    private Object aspectTarget;

    public BoyAbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    //  反射动态调用方法
    protected Object invokeAdviceMethod(BoyJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] paramsTypes = this.aspectMethod.getExceptionTypes();
        if (null == paramsTypes || paramsTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                if (paramsTypes[i] == BoyJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramsTypes[i] == Throwable.class) {
                    args[i] = ex;
                } else if (paramsTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }
}
