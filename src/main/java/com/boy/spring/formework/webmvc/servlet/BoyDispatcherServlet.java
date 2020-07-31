package com.boy.spring.formework.webmvc.servlet;

import com.boy.spring.formework.annotation.BoyController;
import com.boy.spring.formework.annotation.BoyRequestMapping;
import com.boy.spring.formework.context.BoyApplicationContext;
import com.boy.spring.formework.webmvc.BoyHandlerAdapter;
import com.boy.spring.formework.webmvc.BoyHandlerMapping;
import com.boy.spring.formework.webmvc.BoyModelAndView;
import com.boy.spring.formework.webmvc.BoyView;
import com.boy.spring.formework.webmvc.BoyViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // 从页面中输入的 http://localhost/frist.html
        //  解决页面名称和模板文件关联的问题
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);

        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new BoyViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(BoyApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(BoyApplicationContext context) {
    }

    private void initHandlerAdapters(BoyApplicationContext context) {
        // 在初始化阶段，我们能做的就是，将这些参数的名字或者类型按一定的顺序保存下来
        // 因为后面用反射调用的时候，传的形参是一个数组
        // 可以通过记录这些参数的位置 index ，逐个从数组中取值， 这样就和参数的顺序无关了
        for (BoyHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new BoyHandlerAdapter());
        }
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
                    this.handlerMappings.add(new BoyHandlerMapping(pattern, controller, method));
                    System.out.println("Mapping: " + regex + " , " + method);
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            System.out.println("500 error");
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //  根据用户请求的url来获得一个Handler
        BoyHandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new BoyModelAndView("404"));
            return;
        }

        BoyHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        //  这一步只是调用方法，得到返回值
        BoyModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);

        //  这一步是真正的输出
        processDispatchResult(req, resp, modelAndView);
    }

    private BoyHandlerAdapter getHandlerAdapter(BoyHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }

        BoyHandlerAdapter handlerAdapter = this.handlerAdapters.get(handler);
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, BoyModelAndView modelAndView) throws Exception {

        // 调用 viewResolver 的 resolveViewName() 方法
        if (null == modelAndView) {
            return;
        }

        if (this.viewResolvers.isEmpty()) {
            return;
        }

        if (this.viewResolvers != null) {
            for (BoyViewResolver viewResolver : this.viewResolvers) {
                BoyView view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
                if (view != null) {
                    view.render(modelAndView.getModel(), req, resp);
                    return;
                }
            }
        }
    }

    private BoyHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url.replace(contextPath, "").replaceAll("/+", "/");

        for (BoyHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }
}
