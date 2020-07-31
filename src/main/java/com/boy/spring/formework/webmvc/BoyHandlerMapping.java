package com.boy.spring.formework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class BoyHandlerMapping {

    // 目标方法所在的 controller 对象
    private Object controller;

    //  URL 对应的目标方法
    private Method method;

    // url 封装对象
    private Pattern pattern;

    public BoyHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
