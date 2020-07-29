package com.boy.spring.formework.core;

/**
 * 单例工厂的顶层设计
 */
public interface BoyBeanFactory {
    /**
     * 根据beanName 从IOC容器中获得一个实例 Bean
     *
     * @param beanName bean 名称
     * @return 一个实例 Bean
     * @throws Exception 异常
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
