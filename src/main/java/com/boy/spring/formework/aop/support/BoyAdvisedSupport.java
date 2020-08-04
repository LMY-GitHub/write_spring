package com.boy.spring.formework.aop.support;

import com.boy.spring.formework.aop.BoyAopConfig;
import com.boy.spring.formework.aop.aspect.BoyMethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要用来解析和封装AOP配置
 */
public class BoyAdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    private BoyAopConfig config;

    public BoyAdvisedSupport(BoyAopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        // 缓存未命中，则进行下一步处理
        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            //  存入到缓存
            this.methodCache.put(m, cached);
        }
        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    private void parse() {
        //  pointCut表达式
        String pointCut = config.getPointCut()
                .replace("\\.", "\\\\.")
                .replace("\\\\.\\*", ".*")
                .replace("\\(.", "\\\\(.")
                .replace("\\).", "\\\\).");
        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);

        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();
        Pattern pattern = Pattern.compile(pointCut);
        try {
            Class aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<String, Method>();
            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }
            //  在这里得到的方法都是原生方法
            for (Method method : targetClass.getMethods()) {
                String methodString = method.toString();

                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    //  能满足切面规则的类， 添加到AOP配置中
                    List<Object> advices = new LinkedList<Object>();
                    //  前置通知
                    if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore().trim()))) {
                        advices.add(new BoyMethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
                    }
                    //  后置通知
                    if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter().trim()))) {
                        advices.add(new BoyAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
                    }
                    //  异常通知
                    if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow().trim()))) {
                        BoyAfterThrowingAdvice afterThrowingAdvice = advices.add(new BoyAfterThrowingAdvice(aspectMethods.get(config.getAspectAfterThrow()), aspectClass.newInstance()));
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(method, advices);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
