package com.boy.spring.formework.webmvc;

import com.boy.spring.formework.annotation.BoyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BoyHandlerAdapter {
    public BoyModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws InvocationTargetException, IllegalAccessException {
        BoyHandlerMapping handlerMapping = (BoyHandlerMapping) handler;

        // 每个方法有一个参数列表，这里保存的是形参列表
        Map<String, Integer> paramMapping = new HashMap<String, Integer>();

        Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof BoyRequestParam) {
                    String paramName = ((BoyRequestParam) annotation).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        //  根据用户请求的参数信息，跟 Method 中的参数信息进行动态匹配
        //  resp 传进来的目的只有一个：将其赋值给方法参数，经此而已

        //  只有当用户传过来的 ModelAndView 为空的时候，才会新建一个默认的

        //  1、要准备好这个方法的形象列表
        //  方法重载是形参的决定因素：参数的个数，参数的类型，参数的顺序，方法的名字
        //  只处理 Request 和 Response
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }

        /**
         * 2、得到自定义命名参数所在的位置
         * 用户通过URL 传过来的参数列表
         */
        Map<String, String[]> reqParameterMap = req.getParameterMap();

        /**
         * 3、构造实参列表
         */
        Object[] paramValues = new Object[parameterTypes.length];

        for (Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", "");
            if (!paramMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramMapping.get(param.getKey());

            //  因为页面传过来的值都是String类型， 而方法中定义的类型的千变万化的
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        /**
         * 从 handler 中取出 Controller、Method，然后利用反射机制进行调用
         */
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result == null) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == BoyModelAndView.class;

        if (isModelAndView) {
            return (BoyModelAndView) result;
        } else {
            return null;
        }
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == int.class) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }

    public boolean supports(Object handler) {
        return (handler instanceof BoyHandlerMapping);
    }
}
