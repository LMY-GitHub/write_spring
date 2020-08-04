package com.boy.spring.formework.demo.service;

/**
 * 增、删、改业务
 */
public interface IModifyService {
    /**
     * 增加
     */
    public String add(String name, String addr) throws Exception;

    /**
     * 修改
     */
    public String edit(Integer id, String name);

    /**
     * 删除
     */
    public String remove(Integer id);
}
