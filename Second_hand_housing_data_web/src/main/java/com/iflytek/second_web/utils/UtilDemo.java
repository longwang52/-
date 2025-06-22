package com.iflytek.second_web.utils;

import com.iflytek.second_web.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class UtilDemo {

    private static final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    public static void main(String[] args) {
        selectByUsername("zhangsan","123456");
        selectUsers();
        updateUser();
    }


    // 创建dbutils工具类的操作对象
    // 1、对于查询，使用query方法
    // 2、对于增删改，使用update方法
    public static User selectByUsername(String username, String password) {
        // 1、写SQL语句
        String sql = "SELECT * FROM user WHERE username=? AND `pwd`=?";
        try {
            // 2、通过runner执行SQL语句
            User user = runner.query(sql, new BeanHandler<>
                    (User.class), username, password);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("登录出现异常", e);
        }
    }

    // 查询所有
    public static void selectUsers() {
        // 1、写SQL语句
        String sql = "SELECT * FROM user";
        try {
            // 2、通过runner执行SQL语句
            List<User> users = runner.query(sql, new BeanListHandler<User>
                    (User.class));
            for (User user : users) {
                System.out.println(user.getId() + "---" + user.getUsername() + "---" + user.getPwd());
            }
        } catch (SQLException e) {
            throw new RuntimeException("登录出现异常", e);
        }
    }

    // 更新
    public static void updateUser() {
        // 1、写SQL语句
        String sql = "update user set pwd = ? where id = ?";
        try {
            // 2、通过runner执行SQL语句
            int num = runner.update(sql, "123456", 2);
            System.out.println(num);
        } catch (SQLException e) {
            throw new RuntimeException("登录出现异常", e);
        }
    }

}
