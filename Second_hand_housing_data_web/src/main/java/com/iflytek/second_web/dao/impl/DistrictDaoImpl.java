package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.DistrictDao;
import com.iflytek.second_web.domain.District;
import com.iflytek.second_web.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class DistrictDaoImpl implements DistrictDao {
    private final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public List<District> findAllDistricts() {
        String sql = "SELECT district, " +
                "house_count as houseCount, " +
                "CAST(REPLACE(avg_price, ',', '') AS DOUBLE) as avgPrice, " +
                "CAST(REPLACE(max_price, ',', '') AS DOUBLE) as maxPrice, " +
                "CAST(REPLACE(min_price, ',', '') AS DOUBLE) as minPrice, " +
                "CAST(REPLACE(avg_area, ',', '') AS DOUBLE) as avgArea, " +
                "CAST(REPLACE(avg_price_per_sqm, ',', '') AS DOUBLE) as avgPricePerSqm, " +
                "community_count as communityCount " +
                "FROM district_stats ORDER BY house_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(District.class));
        } catch (SQLException e) {
            throw new RuntimeException("查询所有区域失败", e);
        }
    }

    @Override
    public District findByName(String districtName) {
        String sql = "SELECT district, " +
                "house_count as houseCount, " +
                "CAST(REPLACE(avg_price, ',', '') AS DOUBLE) as avgPrice, " +
                "CAST(REPLACE(max_price, ',', '') AS DOUBLE) as maxPrice, " +
                "CAST(REPLACE(min_price, ',', '') AS DOUBLE) as minPrice, " +
                "CAST(REPLACE(avg_area, ',', '') AS DOUBLE) as avgArea, " +
                "CAST(REPLACE(avg_price_per_sqm, ',', '') AS DOUBLE) as avgPricePerSqm, " +
                "community_count as communityCount " +
                "FROM district_stats WHERE district = ?";
        try {
            return runner.query(sql, new BeanHandler<>(District.class), districtName);
        } catch (SQLException e) {
            throw new RuntimeException("按名称查询区域失败", e);
        }
    }

    @Override
    public List<District> findByHouseCountRange(long min, long max) {
        String sql = "SELECT district, " +
                "house_count as houseCount, " +
                "CAST(REPLACE(avg_price, ',', '') AS DOUBLE) as avgPrice, " +
                "CAST(REPLACE(max_price, ',', '') AS DOUBLE) as maxPrice, " +
                "CAST(REPLACE(min_price, ',', '') AS DOUBLE) as minPrice, " +
                "CAST(REPLACE(avg_area, ',', '') AS DOUBLE) as avgArea, " +
                "CAST(REPLACE(avg_price_per_sqm, ',', '') AS DOUBLE) as avgPricePerSqm, " +
                "community_count as communityCount " +
                "FROM district_stats " +
                "WHERE house_count BETWEEN ? AND ? ORDER BY house_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(District.class), min, max);
        } catch (SQLException e) {
            throw new RuntimeException("按房源数量范围查询失败", e);
        }
    }

    @Override
    public List<District> findByAvgPriceAbove(String minPrice) {
        // 清理输入参数中的逗号
        String cleanedMinPrice = minPrice.replace(",", "");

        String sql = "SELECT district, " +
                "house_count as houseCount, " +
                "CAST(REPLACE(avg_price, ',', '') AS DOUBLE) as avgPrice, " +
                "CAST(REPLACE(max_price, ',', '') AS DOUBLE) as maxPrice, " +
                "CAST(REPLACE(min_price, ',', '') AS DOUBLE) as minPrice, " +
                "CAST(REPLACE(avg_area, ',', '') AS DOUBLE) as avgArea, " +
                "CAST(REPLACE(avg_price_per_sqm, ',', '') AS DOUBLE) as avgPricePerSqm, " +
                "community_count as communityCount " +
                "FROM district_stats " +
                "WHERE CAST(REPLACE(avg_price, ',', '') AS DOUBLE) > ? " +
                "ORDER BY CAST(REPLACE(avg_price, ',', '') AS DOUBLE) DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(District.class), Double.parseDouble(cleanedMinPrice));
        } catch (SQLException e) {
            throw new RuntimeException("按平均价格查询失败", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的价格格式: " + minPrice);
        }
    }
}