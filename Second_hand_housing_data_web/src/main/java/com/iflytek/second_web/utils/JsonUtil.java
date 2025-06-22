package com.iflytek.second_web.utils;

import com.iflytek.second_web.domain.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class JsonUtil {
    public static String convertToChartJson(List<District> districts) {
        JSONArray result = new JSONArray();

        for (District district : districts) {
            JSONObject districtJson = new JSONObject();
            // 按照指定顺序添加字段
            districtJson.put("max_price", district.getMaxPrice());
            districtJson.put("min_price", district.getMinPrice());
            districtJson.put("district", district.getDistrict());
            districtJson.put("house_count", district.getHouseCount());
            districtJson.put("avg_area", district.getAvgArea());
            districtJson.put("avg_price", district.getAvgPrice());
            districtJson.put("avg_price_per_sqm", district.getAvgPricePerSqm());
            result.put(districtJson);
        }

        return result.toString();
    }

    public static String convertPriceTrendToJson(List<PriceTrend> trends) {
        JSONArray result = new JSONArray();
        for (PriceTrend trend : trends) {
            JSONObject item = new JSONObject();
            item.put("id", trend.getId() != null ? trend.getId() : 0);
            item.put("year", trend.getYear() != null ? trend.getYear() : 0);
            item.put("district", trend.getDistrict() != null ? trend.getDistrict() : "");
            item.put("houseCount", trend.getHouseCount() != null ? trend.getHouseCount() : 0);
            item.put("avgPrice", trend.getAvgPrice() != null ? trend.getAvgPrice() : 0.0);
            item.put("avgPricePerSqm", trend.getAvgPricePerSqm() != null ? trend.getAvgPricePerSqm() : 0.0);
            item.put("prevAvgPrice", trend.getPrevAvgPrice() != null ? trend.getPrevAvgPrice() : 0.0);
            item.put("priceGrowthRate", trend.getPriceGrowthRate() != null ? trend.getPriceGrowthRate() : 0.0);
            result.put(item);
        }
        return result.toString();
    }

    public static String convertCommunityToJson(List<Community> communities) {
        JSONArray result = new JSONArray();
        for (Community community : communities) {
            JSONObject item = new JSONObject();
            item.put("district", community.getDistrict() != null ?
                    community.getDistrict() : "");
            item.put("community", community.getCommunity() != null ?
                    community.getCommunity() : "");
            item.put("houseCount", community.getHouseCount() != null ?
                    community.getHouseCount() : 0);
            item.put("avgPrice", community.getAvgPrice() != null ?
                    community.getAvgPrice() : "0.00");
            item.put("avgPricePerSqm", community.getAvgPricePerSqm() != null ?
                    community.getAvgPricePerSqm() : "0.00");
            item.put("avgBuildYear", community.getAvgBuildYear() != null ?
                    community.getAvgBuildYear() : "未知");
            result.put(item);
        }
        return result.toString();
    }

    public static String convertInvestmentPotentialToJson(List<InvestmentPotential> potentials) {
        JSONArray result = new JSONArray();
        for (InvestmentPotential potential : potentials) {
            JSONObject item = new JSONObject();
            item.put("district", potential.getDistrict() != null ? potential.getDistrict() : "");
            item.put("community", potential.getCommunity() != null ? potential.getCommunity() : "");
            item.put("transaction_count", potential.getTransactionCount() != null ?
                    potential.getTransactionCount() : 0);
            item.put("avg_price_per_sqm", potential.getAvgPricePerSqm() != null ?
                    potential.getAvgPricePerSqm() : "0.00");
            item.put("potential_level", potential.getPotentialLevel() != null ?
                    potential.getPotentialLevel() : "未知");
            // 新增price_volatility字段输出
            item.put("price_volatility", potential.getPriceVolatility() != null ?
                    potential.getPriceVolatility() : "0.00");
            result.put(item);
        }
        return result.toString();
    }
}