package com.kob.backend.service.impl.user.bot;

import com.kob.backend.dao.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Resource
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = principal.getUser();
        Map<String,String> map=new HashMap<>();
        Integer bot_id=Integer.parseInt(data.get("bot_id"));
        Bot bot=botMapper.selectById(bot_id);
        if(bot==null){
            map.put("error_msg","bot不存在");
            return map;
        }
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        if(title==null||title.length()==0){
            map.put("error_msg","标题不能为空");
            return map;
        }
        if(title.length()>100){
            map.put("error_msg","标题长度不能超过100");
            return map;
        }
        if(description==null||description.length()==0){
            description="";
        }
        if(description.length()>300){
            map.put("error_msg","描述长度不能超过300");
            return map;
        }
        if(content==null||content.length()==0){
            map.put("error_msg","代码不能为空");
            return map;
        }
        if(content.length()>10000){
            map.put("error_msg","代码长度不能超过10000");
            return map;
        }

        if(!user.getId().equals(bot.getUserId())){
            map.put("error_msg","当前用户没有更改权限");
            return map;
        }

        Bot newBot=new Bot(bot.getId(),bot.getUserId(),title,description,
                content,bot.getCreateTime(),new Date());

        botMapper.updateById(newBot);
        map.put("error_msg","success");
        return map;
    }
}
