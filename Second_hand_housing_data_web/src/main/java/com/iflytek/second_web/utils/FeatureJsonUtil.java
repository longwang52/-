package com.iflytek.second_web.utils;

import com.iflytek.second_web.domain.Feature;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class FeatureJsonUtil {
    public static String convertToChartJson(List<Feature> features) {
        JSONObject json = new JSONObject();
        JSONArray areaRangesArray = new JSONArray();
        JSONArray countsArray = new JSONArray();
        JSONArray avgPricesArray = new JSONArray();
        JSONArray avgPricePerSqmArray = new JSONArray();

        for (Feature feature : features) {
            areaRangesArray.put(feature.getAreaRange());
            countsArray.put(feature.getCount());
            avgPricesArray.put(feature.getAvgPrice());
            avgPricePerSqmArray.put(feature.getAvgPricePerSqm());
        }

        json.put("areaRanges", areaRangesArray);
        json.put("counts", countsArray);
        json.put("avgPrices", avgPricesArray);
        json.put("avgPricePerSqm", avgPricePerSqmArray);

        return json.toString();
    }
}