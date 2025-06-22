package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.IUserDao;
import com.iflytek.second_web.dao.impl.UserDaoImpl;
import com.iflytek.second_web.domain.User;
import com.iflytek.second_web.service.IUserService;

/**
 * 用户业务相关 的 业务处理类
 */
public class UserServiceImpl implements IUserService {

    // 在业务类中，定义操作 数据库层的 dao
    private final IUserDao dao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = dao.getUserByName(username, password);
        return user;
    }

    @Override
    public int register(String username, String pwd, String gender) {
        int num = dao.insertUser(username, pwd, gender);
        return num;
    }


}
