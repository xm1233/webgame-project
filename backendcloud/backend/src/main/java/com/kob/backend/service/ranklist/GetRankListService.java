package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.User;

import java.util.List;

public interface GetRankListService {
    JSONObject getRankList(Integer page);
}
