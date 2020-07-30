package com.boy.spring.formework.context;

import com.boy.annotation.BoyAutowired;
import com.boy.annotation.BoyController;
import com.boy.annotation.BoyService;
import com.boy.spring.formework.beans.BoyBeanWrapper;
import com.boy.spring.formework.beans.config.BoyBeanDefinition;
import com.boy.spring.formework.beans.config.BoyBeanPostProcessor;
import com.boy.spring.formework.context.support.BoyBeanDefinitionReader;
import com.boy.spring.formework.context.support.BoyDefaultListableBeanFactory;
import com.boy.spring.formework.core.BoyBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class BoyApplicationContext extends BoyDefaultListableBeanFactory implements BoyBeanFactory {
    private String[] configLocations;

    private BoyBeanDefinitionReader reader;

    //  单例的 IOC 容器缓存
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();

    //  通用的 IOC 容器
    private Map<String, BoyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, BoyBeanWrapper>();

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

    //  只处理不是延时加载的情况
    private void doAutowrited() {
        for (Map.Entry<String, BoyBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<BoyBeanDefinition> beanDefinitions) throws Exception {
        for (BoyBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    public Object getBean(String beanName) throws Exception {
        BoyBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        try {
            //  生成通知事件
            BoyBeanPostProcessor beanPostProcessor = new BoyBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            //  在实例化之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            BoyBeanWrapper beanWrapper = new BoyBeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName, beanWrapper);
            //  在实例化之后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            populateBean(beanName, instance);
            //  通过这样的调用，相当于给了自己留有了可操作的空间
            return this.factoryBeanInstanceCache.get(beanName).getWrappedClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(BoyController.class) || clazz.isAnnotationPresent(BoyService.class))){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(BoyAutowired.class)){
                continue;
            }
            BoyAutowired autowired  = field.getAnnotation(BoyAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                    field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedClass());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //  传入 BeanDefinition ，就返回一个实例
    private Object instantiateBean(BoyBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            if (this.factoryBeanObjectCache.containsKey(className)){
                instance = this.factoryBeanObjectCache.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
