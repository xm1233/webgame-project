package com.kob.backend.service.impl.user.bot;

import com.kob.backend.dao.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Resource
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = principal.getUser();
        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        Map<String,String> map=new HashMap<>();
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

        Date now =new Date();
        Bot bot=new Bot(null,user.getId(),title,description,content,now,now);
        botMapper.insert(bot);
        map.put("error_msg","success");
        return map;
    }
}
