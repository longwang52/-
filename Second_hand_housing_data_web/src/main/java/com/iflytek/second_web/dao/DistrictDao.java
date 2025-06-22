package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.District;
import java.util.List;

/**
 * 区域数据访问接口
 * 功能：定义对district_stats表的CRUD操作
 */
public interface DistrictDao {
    /**
     * 查询所有区域统计数据
     * @return 区域统计列表
     */
    List<District> findAllDistricts();

    /**
     * 根据区域名称查询
     * @param districtName 区域名称
     * @return 区域统计对象
     */
    District findByName(String districtName);

    /**
     * 查询房源数量在指定范围内的区域
     * @param min 最小数量
     * @param max 最大数量
     * @return 符合条件的区域列表
     */
    List<District> findByHouseCountRange(long min, long max);

    /**
     * 查询平均价格高于指定值的区域
     * @param minPrice 最低平均价格
     * @return 符合条件的区域列表
     */
    List<District> findByAvgPriceAbove(String minPrice);
}