package com.iflytek.second_web.service;

import com.iflytek.second_web.domain.User;

public interface IUserService {
    /**
     * 根据用户明和密码 获得对应的一条用户记录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 新增一条记录
     * @param username 用户明
     * @param pwd 密码
     * @param gender 性别
     * @return 影响的行数
     */
    int register(String username, String pwd, String gender);



}
