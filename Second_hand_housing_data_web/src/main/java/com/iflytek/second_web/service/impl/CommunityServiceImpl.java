package com.iflytek.second_web.service.impl;

import com.iflytek.second_web.dao.CommunityDao;
import com.iflytek.second_web.dao.impl.CommunityDaoImpl;
import com.iflytek.second_web.domain.Community;
import com.iflytek.second_web.service.CommunityService;
import java.util.List;

public class CommunityServiceImpl implements CommunityService {
    private CommunityDao communityDao = new CommunityDaoImpl();

    @Override
    public List<Community> getAllCommunities() {
        return communityDao.findAllCommunities();
    }

    @Override
    public List<Community> getCommunitiesByDistrict(String district) {
        return communityDao.findByDistrict(district);
    }

    @Override
    public List<Community> getTopCommunities(int topN) {
        return communityDao.findTopNCommunities(topN);  // 修正了方法名拼写错误
    }
}