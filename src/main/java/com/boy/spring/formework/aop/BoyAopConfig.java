package com.boy.spring.formework.aop;

import lombok.Data;

@Data
public class BoyAopConfig {
    // 以下配置与properties文件中的属性一一对应

    /**
     * 切面表达式
     */
    private String pointCut;

    /**
     * 前置通知方法名
     */
    private String aspectBefore;

    /**
     * 后置通知方法名
     */
    private String aspectAfter;

    /**
     * 异常通知方法
     */
    private String aspectAfterThrow;

    /**
     * 需要通知的异常类型
     */
    private String aspectAfterThrowingName;

    /**
     * 要织入的切面类
     */
    public String aspectClass;
}
