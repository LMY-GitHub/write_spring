package com.boy.spring.formework.context;

import com.boy.spring.formework.beans.BoyBeanWrapper;
import com.boy.spring.formework.beans.config.BoyBeanDefinition;
import com.boy.spring.formework.context.support.BoyBeanDefinitionReader;
import com.boy.spring.formework.context.support.BoyDefaultListableBeanFactory;
import com.boy.spring.formework.core.BoyBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoyApplicationContext extends BoyDefaultListableBeanFactory implements BoyBeanFactory {
    private String[] configLocations;

    private BoyBeanDefinitionReader reader;

    //  单例的 IOC 容器缓存
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();

    //  通用的 IOC 容器
    private Map<String, BoyBeanWrapper> factoryBeanInstance = new ConcurrentHashMap<String, BoyBeanWrapper>();

    public BoyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //  1.定位，定位文件位置
        reader = new BoyBeanDefinitionReader(this.configLocations);

        //  2.加载配置文件，扫描相关的类，把他们封装成 BeanDefinition
        List<BoyBeanDefinition> beanDefinitions = reader.loadBeanDefinition();

        //  3.注册，把配置信息放到容器里面（伪容器）
        doRegisterBeanDefinition(beanDefinitions);

        //  4.把不是延时加载的类提前初始化
        doAutowrited();
    }

    public Object getBean(String beanName) throws Exception {
        return null;
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }
}
