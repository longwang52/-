package com.iflytek.second_web.domain;

public class District {
    private String district;
    private Long houseCount;
    private Double avgPrice;  // 改为Double类型
    private Double maxPrice;  // 改为Double类型
    private Double minPrice;  // 改为Double类型
    private Double avgArea;   // 改为Double类型
    private Double avgPricePerSqm;  // 改为Double类型
    private Integer communityCount;

    public District() {}

    public District(String district, Long houseCount, Double avgPrice,
                    Double maxPrice, Double minPrice, Double avgArea,
                    Double avgPricePerSqm, Integer communityCount) {
        this.district = district;
        this.houseCount = houseCount;
        this.avgPrice = avgPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.avgArea = avgArea;
        this.avgPricePerSqm = avgPricePerSqm;
        this.communityCount = communityCount;
    }

    // getter和setter方法
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(Long houseCount) {
        this.houseCount = houseCount;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getAvgArea() {
        return avgArea;
    }

    public void setAvgArea(Double avgArea) {
        this.avgArea = avgArea;
    }

    public Double getAvgPricePerSqm() {
        return avgPricePerSqm;
    }

    public void setAvgPricePerSqm(Double avgPricePerSqm) {
        this.avgPricePerSqm = avgPricePerSqm;
    }

    public Integer getCommunityCount() {
        return communityCount;
    }

    public void setCommunityCount(Integer communityCount) {
        this.communityCount = communityCount;
    }

    @Override
    public String toString() {
        return "District{" +
                "district='" + district + '\'' +
                ", houseCount=" + houseCount +
                ", avgPrice=" + avgPrice +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", avgArea=" + avgArea +
                ", avgPricePerSqm=" + avgPricePerSqm +
                ", communityCount=" + communityCount +
                '}';
    }
}