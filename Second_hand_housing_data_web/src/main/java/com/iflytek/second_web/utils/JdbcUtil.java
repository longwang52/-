package com.iflytek.second_web.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {
    private static final DruidDataSource dataSource;

    static {
        try {
            // 优先尝试从properties文件加载配置
            InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(p);
            } else {
                // 如果properties文件不存在，使用默认配置
                dataSource = new DruidDataSource();
                // 这里可以设置默认的JDBC配置，或者抛出异常提示需要配置文件
                throw new RuntimeException("druid.properties文件未找到，请确保配置文件存在");
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化数据源失败", e);
        }
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}