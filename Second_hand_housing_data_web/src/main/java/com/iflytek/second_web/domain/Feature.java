package com.iflytek.second_web.domain;

public class Feature {
    private String areaRange;
    private Long count;
    private String avgPrice;
    private String avgPricePerSqm;

    public Feature() {}

    public Feature(String areaRange, Long count, String avgPrice, String avgPricePerSqm) {
        this.areaRange = areaRange;
        this.count = count;
        this.avgPrice = avgPrice;
        this.avgPricePerSqm = avgPricePerSqm;
    }

    // getter和setter方法
    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getAvgPricePerSqm() {
        return avgPricePerSqm;
    }

    public void setAvgPricePerSqm(String avgPricePerSqm) {
        this.avgPricePerSqm = avgPricePerSqm;
    }

    @Override
    public String toString() {
        return "FeatureStats{" +
                "areaRange='" + areaRange + '\'' +
                ", count=" + count +
                ", avgPrice='" + avgPrice + '\'' +
                ", avgPricePerSqm='" + avgPricePerSqm + '\'' +
                '}';
    }
}