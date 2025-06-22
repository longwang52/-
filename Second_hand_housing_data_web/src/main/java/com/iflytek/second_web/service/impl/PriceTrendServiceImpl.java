package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.PriceTrendDao;
import com.iflytek.second_web.dao.impl.PriceTrendDaoImpl;
import com.iflytek.second_web.domain.PriceTrend;
import com.iflytek.second_web.utils.JsonUtil;
import java.util.List;

public class PriceTrendServiceImpl {
    private PriceTrendDao priceTrendDao = new PriceTrendDaoImpl();

    public List<PriceTrend> getAllPriceTrends() {
        return priceTrendDao.findAllPriceTrends();
    }

    public List<PriceTrend> getPriceTrendsByDistrict(String district) {
        return priceTrendDao.findByDistrict(district);
    }

    public List<PriceTrend> getPriceTrendsByYearRange(int startYear, int endYear) {
        return priceTrendDao.findByYearRange(startYear, endYear);
    }

    public String getPriceTrendsJson() {
        List<PriceTrend> trends = getAllPriceTrends();
        return JsonUtil.convertPriceTrendToJson(trends);
    }

    public String getDistrictPriceTrendJson(String district) {
        List<PriceTrend> trends = getPriceTrendsByDistrict(district);
        return JsonUtil.convertPriceTrendToJson(trends);
    }
}