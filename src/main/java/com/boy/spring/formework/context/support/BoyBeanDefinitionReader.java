package com.boy.spring.formework.context.support;

import com.boy.spring.formework.beans.config.BoyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行查找、读取、解析
 */
public class BoyBeanDefinitionReader {

    private List<String> registyBeanClasses = new ArrayList<String>();

    private Properties config = new Properties();

    //  固定配置文件中的 key， 相当于 XML 的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public BoyBeanDefinitionReader(String... locations) {
        //  通过 URL 定位找到其对应的文件，然后装换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        //  转换为文件路径，实际就是把 . 替换为 /
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) continue;
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                registyBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return this.config;
    }

    // 把配置文件中扫描到的所有配置信息转换为BoyBeanDefinition 对象，以便于之后的Ioc 操作
    public List<BoyBeanDefinition> loadBeanDefinition() {
        List<BoyBeanDefinition> result = new ArrayList<BoyBeanDefinition>();

        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) continue;
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //  把每一个配置信息解析成一个 BeanDefinition
    private BoyBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BoyBeanDefinition beanDefinition = new BoyBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    // 将类名首字母改为小写
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //  应为大小写字母的 ASCII 码相差 32
        //  而且大写字母的 ASCII 码要小于小写字母的 ASCII 码
        //  在Java中， 对 char 做算术运算，实际就是对 ASCII 码运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
