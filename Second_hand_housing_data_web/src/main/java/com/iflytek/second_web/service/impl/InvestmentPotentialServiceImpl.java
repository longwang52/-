package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.InvestmentPotentialDao;
import com.iflytek.second_web.dao.impl.InvestmentPotentialDaoImpl;
import com.iflytek.second_web.domain.InvestmentPotential;
import com.iflytek.second_web.utils.JsonUtil;
import java.util.List;

public class InvestmentPotentialServiceImpl {
    private InvestmentPotentialDao investmentDao = new InvestmentPotentialDaoImpl();

    public List<InvestmentPotential> getAllInvestmentPotentials() {
        return investmentDao.findAllInvestmentPotentials();
    }

    public List<InvestmentPotential> getByDistrict(String district) {
        return investmentDao.findByDistrict(district);
    }

    public List<InvestmentPotential> getByPotentialLevel(String level) {
        return investmentDao.findByPotentialLevel(level);
    }

    public String getInvestmentPotentialJson() {
        List<InvestmentPotential> potentials = getAllInvestmentPotentials();
        return JsonUtil.convertInvestmentPotentialToJson(potentials);
    }
}