package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.Community;
import java.util.List;

/**
 * 小区数据访问接口
 * 功能：定义对community_stats表的操作
 */
public interface CommunityDao {
    /**
     * 查询所有小区统计数据
     * @return 小区统计列表
     */
    List<Community> findAllCommunities();

    /**
     * 根据区域查询小区
     * @param district 区域名称
     * @return 该区域下的小区列表
     */
    List<Community> findByDistrict(String district);

    /**
     * 查询房源数量排名前N的小区
     * @param topN 前N名
     * @return 热门小区列表
     */
    List<Community> findTopNCommunities(int topN);
}