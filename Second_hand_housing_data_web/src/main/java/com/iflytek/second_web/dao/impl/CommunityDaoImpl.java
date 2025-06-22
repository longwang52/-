package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.CommunityDao;
import com.iflytek.second_web.domain.Community;
import com.iflytek.second_web.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;
import java.util.List;

public class CommunityDaoImpl implements CommunityDao {
    private final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public List<Community> findAllCommunities() {
        String sql = "SELECT " +
                "district, community, " +
                "house_count AS houseCount, " +
                "avg_price AS avgPrice, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "avg_build_year AS avgBuildYear " +
                "FROM community_stats ORDER BY house_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(Community.class));
        } catch (SQLException e) {
            throw new RuntimeException("查询所有小区数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Community> findByDistrict(String district) {
        String sql = "SELECT " +
                "district, community, " +
                "house_count AS houseCount, " +
                "avg_price AS avgPrice, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "avg_build_year AS avgBuildYear " +
                "FROM community_stats WHERE district = ? ORDER BY house_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(Community.class), district);
        } catch (SQLException e) {
            throw new RuntimeException("按区域查询小区数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Community> findTopNCommunities(int topN) {
        String sql = "SELECT " +
                "district, community, " +
                "house_count AS houseCount, " +
                "avg_price AS avgPrice, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "avg_build_year AS avgBuildYear " +
                "FROM community_stats ORDER BY house_count DESC LIMIT ?";
        try {
            return runner.query(sql, new BeanListHandler<>(Community.class), topN);
        } catch (SQLException e) {
            throw new RuntimeException("查询热门小区失败: " + e.getMessage(), e);
        }
    }
}