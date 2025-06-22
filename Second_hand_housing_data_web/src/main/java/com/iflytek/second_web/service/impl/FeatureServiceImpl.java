package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.FeatureDao;
import com.iflytek.second_web.dao.impl.FeatureDaoImpl;
import com.iflytek.second_web.domain.Feature;
import com.iflytek.second_web.utils.FeatureJsonUtil;
import java.util.List;

public class FeatureServiceImpl {
    private FeatureDao featureDao = new FeatureDaoImpl();

    public List<Feature> getAllFeatures() {
        return featureDao.findAll();
    }

    public Feature getFeatureByAreaRange(String areaRange) {
        return featureDao.findByAreaRange(areaRange);
    }

    public List<Feature> getFeaturesByCountRange(long min, long max) {
        return featureDao.findByCountRange(min, max);
    }

    public List<Feature> getFeaturesByPricePerSqmAbove(String minPricePerSqm) {
        return featureDao.findByPricePerSqmAbove(minPricePerSqm);
    }

    public String getFeatureStatsJson() {
        List<Feature> features = getAllFeatures();
        return FeatureJsonUtil.convertToChartJson(features);
    }

    public String getPricePerSqmStatsJson(String minPricePerSqm) {
        List<Feature> features = getFeaturesByPricePerSqmAbove(minPricePerSqm);
        return FeatureJsonUtil.convertToChartJson(features);
    }
}