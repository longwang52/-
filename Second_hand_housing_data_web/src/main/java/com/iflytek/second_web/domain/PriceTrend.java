package com.iflytek.second_web.domain;

/**
 * 价格趋势模型类
 */
public class PriceTrend {
    private Integer id;
    private Integer year;
    private String district;
    private Long houseCount;
    private Double avgPrice;
    private Double avgPricePerSqm;
    private Double prevAvgPrice;
    private Double priceGrowthRate;

    // 无参构造
    public PriceTrend() {}

    // 全参构造
    public PriceTrend(Integer id, Integer year, String district, Long houseCount,
                      Double avgPrice, Double avgPricePerSqm, Double prevAvgPrice,
                      Double priceGrowthRate) {
        this.id = id;
        this.year = year;
        this.district = district;
        this.houseCount = houseCount;
        this.avgPrice = avgPrice;
        this.avgPricePerSqm = avgPricePerSqm;
        this.prevAvgPrice = prevAvgPrice;
        this.priceGrowthRate = priceGrowthRate;
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

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

    public Double getAvgPricePerSqm() {
        return avgPricePerSqm;
    }

    public void setAvgPricePerSqm(Double avgPricePerSqm) {
        this.avgPricePerSqm = avgPricePerSqm;
    }

    public Double getPrevAvgPrice() {
        return prevAvgPrice;
    }

    public void setPrevAvgPrice(Double prevAvgPrice) {
        this.prevAvgPrice = prevAvgPrice;
    }

    public Double getPriceGrowthRate() {
        return priceGrowthRate;
    }

    public void setPriceGrowthRate(Double priceGrowthRate) {
        this.priceGrowthRate = priceGrowthRate;
    }

    @Override
    public String toString() {
        return "PriceTrend{" +
                "id=" + id +
                ", year=" + year +
                ", district='" + district + '\'' +
                ", houseCount=" + houseCount +
                ", avgPrice=" + avgPrice +
                ", avgPricePerSqm=" + avgPricePerSqm +
                ", prevAvgPrice=" + prevAvgPrice +
                ", priceGrowthRate=" + priceGrowthRate +
                '}';
    }
}