package com.iflytek.second_web.dao.impl;

import com.iflytek.second_web.dao.InvestmentPotentialDao;
import com.iflytek.second_web.domain.InvestmentPotential;
import com.iflytek.second_web.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;
import java.util.List;

public class InvestmentPotentialDaoImpl implements InvestmentPotentialDao {
    private final QueryRunner runner = new QueryRunner(JdbcUtil.getDataSource());

    @Override
    public List<InvestmentPotential> findAllInvestmentPotentials() {
        String sql = "SELECT " +
                "district, " +
                "community, " +
                "transaction_count AS transactionCount, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "potential_level AS potentialLevel, " +
                "price_volatility AS priceVolatility " +
                "FROM investment_potential ORDER BY transaction_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(InvestmentPotential.class));
        } catch (SQLException e) {
            throw new RuntimeException("查询所有投资潜力数据失败", e);
        }
    }

    @Override
    public List<InvestmentPotential> findByDistrict(String district) {
        String sql = "SELECT " +
                "district, " +
                "community, " +
                "transaction_count AS transactionCount, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "potential_level AS potentialLevel, " +
                "price_volatility AS priceVolatility " +
                "FROM investment_potential WHERE district = ? ORDER BY transaction_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(InvestmentPotential.class), district);
        } catch (SQLException e) {
            throw new RuntimeException("按区域查询投资潜力数据失败", e);
        }
    }

    @Override
    public List<InvestmentPotential> findByPotentialLevel(String level) {
        String sql = "SELECT " +
                "district, " +
                "community, " +
                "transaction_count AS transactionCount, " +
                "avg_price_per_sqm AS avgPricePerSqm, " +
                "potential_level AS potentialLevel, " +
                "price_volatility AS priceVolatility " +
                "FROM investment_potential WHERE potential_level = ? ORDER BY transaction_count DESC";
        try {
            return runner.query(sql, new BeanListHandler<>(InvestmentPotential.class), level);
        } catch (SQLException e) {
            throw new RuntimeException("按潜力等级查询投资潜力数据失败", e);
        }
    }
}