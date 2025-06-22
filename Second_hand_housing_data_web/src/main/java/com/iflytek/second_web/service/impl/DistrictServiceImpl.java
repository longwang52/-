package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.DistrictDao;
import com.iflytek.second_web.dao.impl.DistrictDaoImpl;
import com.iflytek.second_web.domain.District;
import com.iflytek.second_web.utils.JsonUtil;
import java.util.List;

public class DistrictServiceImpl {
    private DistrictDao districtDao = new DistrictDaoImpl();

    public List<District> getAllDistricts() {
        return districtDao.findAllDistricts();
    }

    public District getDistrictByName(String name) {
        return districtDao.findByName(name);
    }

    public List<District> getDistrictsByHouseCountRange(long min, long max) {
        return districtDao.findByHouseCountRange(min, max);
    }

    public List<District> getDistrictsByAvgPriceAbove(String minPrice) {
        return districtDao.findByAvgPriceAbove(minPrice);
    }

    public String getDistrictStatsJson() {
        List<District> districts = getAllDistricts();
        return JsonUtil.convertToChartJson(districts);
    }

    public String getPriceRangeDistrictsJson(String minPrice) {
        List<District> districts = getDistrictsByAvgPriceAbove(minPrice);
        return JsonUtil.convertToChartJson(districts);
    }
}