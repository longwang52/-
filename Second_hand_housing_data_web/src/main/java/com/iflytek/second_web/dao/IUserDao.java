package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.User;

public interface IUserDao {
    /**
     * 根据用户名、密码 查询数据
     * @param username
     * @param password
     * @return
     */
    User getUserByName(String username, String password);

    /**
     * 新增一条记录
     * @param username
     * @param password
     * @param gender
     * @return
     */
    int insertUser(String username, String password, String gender);
}
