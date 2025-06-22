package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.FeatureDao;
import com.iflytek.second_web.domain.Feature;
import com.iflytek.second_web.utils.JdbcUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeatureDaoImpl implements FeatureDao {
    @Override
    public List<Feature> findAll() {
        String sql = "SELECT area_range, count, avg_price, avg_price_per_sqm FROM feature_stats";
        return executeQuery(sql);
    }

    @Override
    public Feature findByAreaRange(String areaRange) {
        String sql = "SELECT area_range, count, avg_price, avg_price_per_sqm FROM feature_stats WHERE area_range = ?";
        List<Feature> list = executeQuery(sql, areaRange);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Feature> findByCountRange(long min, long max) {
        String sql = "SELECT area_range, count, avg_price, avg_price_per_sqm FROM feature_stats WHERE count BETWEEN ? AND ?";
        return executeQuery(sql, min, max);
    }

    @Override
    public List<Feature> findByPricePerSqmAbove(String minPricePerSqm) {
        String sql = "SELECT area_range, count, avg_price, avg_price_per_sqm FROM feature_stats WHERE avg_price_per_sqm >= ?";
        return executeQuery(sql, minPricePerSqm);
    }

    private List<Feature> executeQuery(String sql, Object... params) {
        List<Feature> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Feature feature = new Feature();
                    feature.setAreaRange(rs.getString("area_range"));
                    feature.setCount(rs.getLong("count"));
                    feature.setAvgPrice(rs.getString("avg_price"));
                    feature.setAvgPricePerSqm(rs.getString("avg_price_per_sqm"));
                    list.add(feature);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}