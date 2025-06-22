package com.iflytek.second_web.service;

import com.iflytek.second_web.domain.Community;
import java.util.List;

public interface CommunityService {
    List<Community> getAllCommunities();
    List<Community> getCommunitiesByDistrict(String district);
    List<Community> getTopCommunities(int topN);
}