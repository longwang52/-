package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.PriceTrendDao;
import com.iflytek.second_web.domain.PriceTrend;
import com.iflytek.second_web.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class PriceTrendDaoImpl implements PriceTrendDao {
    private final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public List<PriceTrend> findAllPriceTrends() {
        String sql = "SELECT * FROM price_trend ORDER BY year, district";
        try {
            return runner.query(sql, new BeanListHandler<>(PriceTrend.class));
        } catch (SQLException e) {
            throw new RuntimeException("查询所有价格趋势失败", e);
        }
    }

    @Override
    public List<PriceTrend> findByDistrict(String district) {
        String sql = "SELECT * FROM price_trend WHERE district = ? ORDER BY year";
        try {
            return runner.query(sql, new BeanListHandler<>(PriceTrend.class), district);
        } catch (SQLException e) {
            throw new RuntimeException("按区域查询价格趋势失败", e);
        }
    }

    @Override
    public List<PriceTrend> findByYearRange(int startYear, int endYear) {
        String sql = "SELECT * FROM price_trend WHERE year BETWEEN ? AND ? ORDER BY year, district";
        try {
            return runner.query(sql, new BeanListHandler<>(PriceTrend.class), startYear, endYear);
        } catch (SQLException e) {
            throw new RuntimeException("按年份范围查询价格趋势失败", e);
        }
    }
}