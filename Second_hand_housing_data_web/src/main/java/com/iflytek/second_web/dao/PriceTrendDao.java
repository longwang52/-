package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.PriceTrend;
import java.util.List;

/**
 * 价格趋势数据访问接口
 * 功能：定义对price_trend表的操作
 */
public interface PriceTrendDao {
    /**
     * 查询所有价格趋势数据
     * @return 价格趋势列表
     */
    List<PriceTrend> findAllPriceTrends();

    /**
     * 按区域查询价格趋势
     * @param district 区域名称
     * @return 该区域的价格趋势
     */
    List<PriceTrend> findByDistrict(String district);

    /**
     * 按年份范围查询价格趋势
     * @param startYear 起始年份
     * @param endYear 结束年份
     * @return 指定年份范围内的价格趋势
     */
    List<PriceTrend> findByYearRange(int startYear, int endYear);
}