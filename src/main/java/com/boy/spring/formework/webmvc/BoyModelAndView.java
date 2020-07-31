package com.boy.spring.formework.webmvc;

import java.util.Map;

public class BoyModelAndView {
    /**
     * 页面模板的名称
     */
    public String viewName;

    /**
     * 往页面传送的参数
     */
    private Map<String, ?> model;

    public BoyModelAndView(String viewName) {
        this(viewName, null);
    }

    public BoyModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
