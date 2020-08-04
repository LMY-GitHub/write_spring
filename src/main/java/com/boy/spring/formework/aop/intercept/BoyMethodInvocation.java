package com.boy.spring.formework.aop.intercept;

import com.boy.spring.formework.aop.aspect.BoyJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行拦截器链，相当于 Spring 中的 ReflectiveMethodInvocation 的功能
 */
public class BoyMethodInvocation implements BoyJoinPoint {
    /**
     * 代理对象
     */
    private Object proxy;

    /**
     * 代理的目标方法
     */
    private Method method;

    /**
     * 代理的目标对象
     */
    private Object target;

    /**
     * 代理的目标类
     */
    private Class<?> targetClass;

    /**
     * 代理的方法的实参列表
     */
    private Object[] arguments;

    /**
     * 回调方法链
     */
    private List<Object> interceptorsAndDynamicMethodMatchers;

    /**
     * 保存的自定义属性
     */
    private Map<String, Object> userAttributes;

    private int currentInterceptorIndex = -1;

    public BoyMethodInvocation(Object proxy, Method method, Object target, Class<?> targetClass, Object[] arguments, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }


    public Object proceed() throws Throwable {
        //  如果 Interceptor 执行完了，则执行 joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        //  如果动态匹配 joinPoint
        if (interceptorOrInterceptionAdvice instanceof BoyMethodInterceptor) {
            BoyMethodInterceptor mi = (BoyMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        } else {
            //  执行当前 Intercetpor
            return proceed();
        }
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public Object getThis() {
        return this.target;
    }

    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<String, Object>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    public Object getUserAttribute(String key) {
        return (this.userAttributes != null ? this.userAttributes.get(key) : null);
    }
}
