package com.boy.spring.formework.demo.service.impl;

import com.boy.spring.formework.annotation.BoyService;
import com.boy.spring.formework.demo.service.IModifyService;

@BoyService
public class ModifyService implements IModifyService {
    public String add(String name, String addr) {
        return "modifyService add, name = " + name + "addr = " + addr;
    }

    public String edit(Integer id, String name) {
        return "modifyService edit, id = " + id + "name = " + name;
    }

    public String remove(Integer id) {
        return "modifyService remove, id = " + id;
    }
}
