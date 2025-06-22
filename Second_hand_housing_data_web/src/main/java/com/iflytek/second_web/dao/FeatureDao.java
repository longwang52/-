package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.Feature;
import java.util.List;

public interface FeatureDao {
    List<Feature> findAll();
    Feature findByAreaRange(String areaRange);
    List<Feature> findByCountRange(long min, long max);
    List<Feature> findByPricePerSqmAbove(String minPricePerSqm);
}