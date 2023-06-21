package com.kob.backend.service.impl.user.bot;

import com.kob.backend.dao.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {

    @Resource
    private BotMapper botMapper;


    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = principal.getUser();

        int bot_id=Integer.parseInt(data.get("bot_id"));
        Bot bot=botMapper.selectById(bot_id);

        Map<String,String>map=new HashMap<>();

        if(bot==null){
            map.put("error_msg","bot不存在");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_msg","当前用户没有删除权限");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("error_msg","success");
        return map;
    }
}
