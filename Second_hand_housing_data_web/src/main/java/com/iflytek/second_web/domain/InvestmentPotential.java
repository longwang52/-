package com.iflytek.second_web.domain;

/**
 * 投资潜力实体类
 * 对应数据库中的 investment_potential 表
 */
public class InvestmentPotential {
    private String district;          // 区域名称
    private String community;         // 小区名称
    private String potentialLevel;    // 潜力等级
    private Long transactionCount;    // 交易数量
    private String avgPricePerSqm;    // 每平米均价
    private String priceVolatility;   // 价格波动率

    // 无参构造方法
    public InvestmentPotential() {}

    // 全参构造方法
    public InvestmentPotential(String district, String community, String potentialLevel,
                               Long transactionCount, String avgPricePerSqm, String priceVolatility) {
        this.district = district;
        this.community = community;
        this.potentialLevel = potentialLevel;
        this.transactionCount = transactionCount;
        this.avgPricePerSqm = avgPricePerSqm;
        this.priceVolatility = priceVolatility;
    }

    // Getter 和 Setter 方法
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

    public String getPotentialLevel() {
        return potentialLevel;
    }

    public void setPotentialLevel(String potentialLevel) {
        this.potentialLevel = potentialLevel;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getAvgPricePerSqm() {
        return avgPricePerSqm;
    }

    public void setAvgPricePerSqm(String avgPricePerSqm) {
        this.avgPricePerSqm = avgPricePerSqm;
    }

    public String getPriceVolatility() {
        return priceVolatility;
    }

    public void setPriceVolatility(String priceVolatility) {
        this.priceVolatility = priceVolatility;
    }

    // toString 方法
    @Override
    public String toString() {
        return "InvestmentPotential{" +
                "district='" + district + '\'' +
                ", community='" + community + '\'' +
                ", potentialLevel='" + potentialLevel + '\'' +
                ", transactionCount=" + transactionCount +
                ", avgPricePerSqm='" + avgPricePerSqm + '\'' +
                ", priceVolatility='" + priceVolatility + '\'' +
                '}';
    }
}