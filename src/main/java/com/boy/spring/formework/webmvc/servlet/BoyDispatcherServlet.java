package com.boy.spring.formework.webmvc.servlet;

import com.boy.annotation.BoyController;
import com.boy.annotation.BoyRequestMapping;
import com.boy.spring.formework.context.BoyApplicationContext;
import com.boy.spring.formework.webmvc.BoyHandlerAdapter;
import com.boy.spring.formework.webmvc.BoyHandlerMapping;
import com.boy.spring.formework.webmvc.BoyViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SL
public class BoyDispatcherServlet extends HttpServlet {
    private final String LOCATION = "contextConfigLocation";

    private List<BoyHandlerMapping> handlerMappings = new ArrayList<BoyHandlerMapping>();

    private Map<BoyHandlerMapping, BoyHandlerAdapter> handlerAdapters = new HashMap<BoyHandlerMapping, BoyHandlerAdapter>();

    private List<BoyViewResolver> viewResolvers = new ArrayList<BoyViewResolver>();

    private BoyApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //  相当于把 IOC 容器初始化了
        context = new BoyApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(BoyApplicationContext context) {
        /**
         * 有九种策略
         * 针对每一种用户请求，都会经过一些处理策略处理，最终才能有结果输出
         * 每种策略可以自定义干预，但是最终的输出结果是一致的
         */
        //  文件上传解析，如果是请求类型 multipart ， 将通过MultipartResolver 进行文件上传解析
        initMultipartResolver(context);

        //  本地化解析
        initLocaleResolver(context);

        //  主题解析
        initThemeResolver(context);

        // BoyHandlerMapping 用来保存 Controller 中配置的 RequestMapping 和 Method 的对应关系
        //  通过 HandlerMapping 将请求映射到处理器中
        initHandlerMappings(context);

        //  HandlerAdapters 用来动态匹配 Method 参数，包括类转换，动态赋值
        //  通过 HandlerAdapter 进行多类型的参数动态匹配
        initHandlerAdapters(context);

        //  如果运行中遇到异常，酱油本方法来解析
        initHandlerExceptionResolvers(context);

        //  直接将请求解析到试图名
        initRequestToViewNameTranslator(context);

        //  通过ViewResolvers实现动态模板的解析
        //  自己解析的一套模板语言
        initViewResolvers(context);

        //  Flash 映射管理器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(BoyApplicationContext context) {
    }

    private void initViewResolvers(BoyApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(BoyApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(BoyApplicationContext context) {
    }

    private void initHandlerAdapters(BoyApplicationContext context) {
    }

    /**
     * 将 Controller 中配置的 RequestMapping 和 Method 进行一一对应
     */
    private void initHandlerMappings(BoyApplicationContext context) {

        //  从容器中获取到所有的实例
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(BoyController.class)) {
                    continue;
                }
                String beanUrl = "";

                if (clazz.isAnnotationPresent(BoyRequestMapping.class)) {
                    BoyRequestMapping requestMapping = clazz.getAnnotation(BoyRequestMapping.class);
                    beanUrl = requestMapping.value();
                }

                //  扫描所有的 public 类型的方法
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(BoyRequestMapping.class)) {
                        continue;
                    }
                    BoyRequestMapping requestMapping = method.getAnnotation(BoyRequestMapping.class);
                    String regex = ("/" + beanUrl + requestMapping.value().replaceAll("\\*", ".*"))
                            .replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this,handlerMappings.add(new BoyHandlerMapping(pattern,controller,method));
                    log.info
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initThemeResolver(BoyApplicationContext context) {
    }

    private void initLocaleResolver(BoyApplicationContext context) {
    }

    private void initMultipartResolver(BoyApplicationContext context) {
    }

}
