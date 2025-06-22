package com.iflytek.second_web.dao;

import com.iflytek.second_web.domain.InvestmentPotential;
import java.util.List;

public interface InvestmentPotentialDao {
    List<InvestmentPotential> findAllInvestmentPotentials();
    List<InvestmentPotential> findByDistrict(String district);
    List<InvestmentPotential> findByPotentialLevel(String level);
}