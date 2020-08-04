package com.boy.spring.formework.aop;

import com.boy.spring.formework.aop.intercept.BoyMethodInterceptor;
import com.boy.spring.formework.aop.intercept.BoyMethodInvocation;
import com.boy.spring.formework.aop.support.BoyAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 使用 JDK Proxy API 生成代理类
 */
public class BoyJdkDynamicAopProxy implements BoyAopProxy, InvocationHandler {
    private BoyAdvisedSupport config;

    public BoyJdkDynamicAopProxy(BoyAdvisedSupport config) {
        this.config = config;
    }

    //  把原生的对象传进来
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }

    //  invoke() 方法是执行代理的关键入口
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //  将每一个 JoinPoint 也就是被代理的业务方法 （Method） 封装成一个连接器，组成一个拦截器链
        List<Object> interceptorsAndDynamicMethodMatchers = config.getInterceptorAndDynamicInterceptionAdvice(method, this.config.getTargetClass());

        //  交给拦截链MethodInvocation 的proceed() 方法执行
        BoyMethodInvocation invocation = new BoyMethodInvocation(proxy, method, this.config.getTarget(), this.config.getTargetClass(), args, interceptorsAndDynamicMethodMatchers);

        return invocation.proceed();
    }
}
