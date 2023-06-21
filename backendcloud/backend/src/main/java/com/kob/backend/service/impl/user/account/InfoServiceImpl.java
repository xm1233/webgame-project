package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = principal.getUser();

        Map<String,String> map=new HashMap<>();
        map.put("error_msg","success");
        map.put("id",user.getId().toString());
        map.put("username",user.getUserName());
        map.put("photo",user.getPhoto());
        return map;
    }
}
