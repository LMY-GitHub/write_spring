package com.boy.spring.formework.demo.action;

import com.boy.spring.formework.annotation.BoyAutowired;
import com.boy.spring.formework.annotation.BoyController;
import com.boy.spring.formework.annotation.BoyRequestMapping;
import com.boy.spring.formework.annotation.BoyRequestParam;
import com.boy.spring.formework.demo.service.IModifyService;
import com.boy.spring.formework.demo.service.IQueryService;
import com.boy.spring.formework.webmvc.BoyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@BoyController
@BoyRequestMapping("/web")
public class MyAction {
    @BoyAutowired
    IQueryService queryService;

    @BoyAutowired
    IModifyService modifyService;

    @BoyRequestMapping("/query.json")
    public BoyModelAndView query(HttpServletRequest req, HttpServletResponse resp,
                                 @BoyRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(resp, result);
    }

    @BoyRequestMapping("/add*.json")
    public BoyModelAndView add(HttpServletRequest req, HttpServletResponse resp,
                               @BoyRequestParam("name") String name, @BoyRequestParam("addr") String addr) {
        String result = modifyService.add(name, addr);
        return out(resp, result);
    }

    @BoyRequestMapping("/remove.json")
    public BoyModelAndView add(HttpServletRequest req, HttpServletResponse resp,
                               @BoyRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(resp, result);
    }


    @BoyRequestMapping("/edit.json")
    public BoyModelAndView add(HttpServletRequest req, HttpServletResponse resp,
                               @BoyRequestParam("id") Integer id, @BoyRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(resp, result);
    }

    private BoyModelAndView out(HttpServletResponse resp, String result) {
        try {
            resp.getWriter().write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
