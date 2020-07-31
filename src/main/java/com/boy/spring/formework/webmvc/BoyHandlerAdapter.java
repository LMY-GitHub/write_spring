package com.boy.spring.formework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoyHandlerAdapter {
    public BoyModelAndView handle(HttpServletRequest req, HttpServletResponse resp, BoyHandlerMapping handler) {
        return null;
    }

    public boolean supports(BoyHandlerMapping handler) {
        return false;
    }
}
