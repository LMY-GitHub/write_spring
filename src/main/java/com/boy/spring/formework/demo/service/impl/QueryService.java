package com.boy.spring.formework.demo.service.impl;

import com.boy.spring.formework.annotation.BoyService;
import com.boy.spring.formework.demo.service.IQueryService;

import java.text.SimpleDateFormat;
import java.util.Date;

@BoyService
public class QueryService implements IQueryService {
    public String query(String name) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        String json = "{name:\"" + name + "\", time:\"" + time + "\" }";
        System.out.println("这是业务方法中打印的：" + json);
        return json;
    }
}
