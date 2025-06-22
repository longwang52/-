package com.iflytek.second_web.domain;

public class Community {
    private String district;
    private String community;
    private Long houseCount;
    private String avgPrice;
    private String avgPricePerSqm;
    private String avgBuildYear;

    public Community() {}

    public Community(String district, String community, Long houseCount,
                     String avgPrice, String avgPricePerSqm, String avgBuildYear) {
        this.district = district;
        this.community = community;
        this.houseCount = houseCount;
        this.avgPrice = avgPrice;
        this.avgPricePerSqm = avgPricePerSqm;
        this.avgBuildYear = avgBuildYear;
    }

    // Getters and Setters
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public Long getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(Long houseCount) {
        this.houseCount = houseCount;
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

    public String getAvgBuildYear() {
        return avgBuildYear;
    }

    public void setAvgBuildYear(String avgBuildYear) {
        this.avgBuildYear = avgBuildYear;
    }

    @Override
    public String toString() {
        return "Community{" +
                "district='" + district + '\'' +
                ", community='" + community + '\'' +
                ", houseCount=" + houseCount +
                ", avgPrice='" + avgPrice + '\'' +
                ", avgPricePerSqm='" + avgPricePerSqm + '\'' +
                ", avgBuildYear='" + avgBuildYear + '\'' +
                '}';
    }
}