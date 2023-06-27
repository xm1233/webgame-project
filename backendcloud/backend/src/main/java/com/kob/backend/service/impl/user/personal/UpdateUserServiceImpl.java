package com.kob.backend.service.impl.user.personal;

import com.kob.backend.dao.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.personal.UpdateUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> updateUser(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = principal.getUser();
        Map<String,String> map=new HashMap<>();
        String userName=data.get("user_username");
        String photo=data.get("photo");
        String passWord=data.get("password");
        String confirmedPassWord=data.get("confirmpassword");
        if(userName==null||userName.length()==0){
            map.put("error_msg","用户名不能为空");
            return map;
        }
        if(passWord.length()==0||confirmedPassWord.length()==0){
            map.put("error_msg","密码长度不能为空");
            return map;
        }
        if(userName.length()>100){
            map.put("error_msg","用户名长度过长");
            return map;
        }
        if(passWord.length()>100||confirmedPassWord.length()>100){
            map.put("error_msg","密码长度过长");
            return map;
        }
        if(!passWord.equals(confirmedPassWord)){
            map.put("error_msg","两次输入密码不一致");
            return map;
        }

        String encodedPassWord=passwordEncoder.encode(passWord);

        User newUser=new User(user.getId(),userName,encodedPassWord,photo,user.getRating());
        userMapper.updateById(newUser);
        map.put("error_msg","success");
        return map;
    }
}
