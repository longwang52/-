package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.IUserDao;
import com.iflytek.second_web.domain.User;
import com.iflytek.second_web.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserDaoImpl implements IUserDao {

    // 创建dbutils工具类的操作对象
    // 1、对于查询，使用query方法
    // 2、对于增删改，使用update方法
    private final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public User getUserByName(String username, String password) {
        // 1、写SQL语句
        String sql = "SELECT * FROM user WHERE username=? AND pwd=?";
        try {
            // 2、通过runner执行SQL语句
            User user = runner.query(sql, new BeanHandler<User>
                    (User.class), username, password);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("登录出现异常", e);
        }

    }

    @Override
    public int insertUser(String username, String password, String gender) {
        String dbGender = "1";
        if ("female".equals(gender)) {
            dbGender = "0";
        }
        String sql = "INSERT INTO user(username,pwd,gender) VALUES(?,?,?)";
        int num = 0;
        try {
            num = runner.update(sql, username, password, dbGender);
        } catch (SQLException e) {
            throw new RuntimeException("登录出现异常", e);
        }
        return num;
    }
}
