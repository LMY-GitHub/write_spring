package com.boy.spring.formework.aop.aspect;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 定义一个织入的切面逻辑，也就是要针对目标代理对象增强的逻辑
 * 本类主要完成对方法调用的监控，监听目标方法每次执行所消耗的时间
 */
@Slf4j
public class LogAspect {
    //  在调用一个方法之前，执行before()方法
    public void before(BoyJoinPoint joinPoint) {
        joinPoint.setUserAttribute("startTine_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        log.info("Invoker Before Method!!!" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //  在调用一个方法之后，after()方法
    public void after(BoyJoinPoint joinPoint) {
        log.info("Invoker After Method!!!" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTine_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
    }

    public void afterThrowing(BoyJoinPoint joinPoint, Throwable ex) {
        log.info("出现异常" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()) + "\nThrows:" + ex.getMessage());
    }
}
