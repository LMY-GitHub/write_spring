package com.boy.spring.formework.demo.action;

import com.boy.spring.formework.annotation.BoyAutowired;
import com.boy.spring.formework.annotation.BoyController;
import com.boy.spring.formework.annotation.BoyRequestMapping;
import com.boy.spring.formework.annotation.BoyRequestParam;
import com.boy.spring.formework.demo.service.IQueryService;
import com.boy.spring.formework.webmvc.BoyModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口 URL
 */
@BoyController
@BoyRequestMapping("/")
public class PageAction {
    @BoyAutowired
    IQueryService queryService;


    @BoyRequestMapping("/first.html")
    public BoyModelAndView query(@BoyRequestParam("name") String name) {
        String result = queryService.query(name);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("teacher", name);
        model.put("data", result);
        model.put("token", "123456");
        return new BoyModelAndView("first.html", model);
    }
}
