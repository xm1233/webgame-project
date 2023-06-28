package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.dao.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {

    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getRankList(Integer page) {
        IPage<User> userIPage=new Page<>(page,10);
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, userQueryWrapper).getRecords();
        JSONObject resp=new JSONObject();
        for (User user:users){
            user.setPassWord("");
        }
        resp.put("ranklist",users);
        resp.put("user_count",userMapper.selectCount(null));
        return resp;
    }
}
