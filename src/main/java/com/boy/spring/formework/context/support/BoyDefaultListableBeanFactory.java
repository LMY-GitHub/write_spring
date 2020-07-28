package com.boy.spring.formework.context.support;

import com.boy.spring.formework.beans.config.BoyBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoyDefaultListableBeanFactory extends BoyAbstractApplicationContext {
    /**
     * 存储注册信息的 BeanDefinition
     */
    protected final Map<String, BoyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BoyBeanDefinition>();
}
