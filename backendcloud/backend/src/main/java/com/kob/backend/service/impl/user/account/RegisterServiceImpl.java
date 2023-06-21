package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.dao.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String userName, String passWord, String confirmedPassWord) {
        Map<String,String> map=new HashMap<>();
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<User>();
        userQueryWrapper.eq("user_name",userName);
        List<User> userList= userMapper.selectList(userQueryWrapper);
        if(!userList.isEmpty()){
            map.put("error_msg","用户名已存在,请重新输入");
            return map;
        }
        if(userName==null){
            map.put("error_msg","用户名不能为空");
            return map;
        }
        if(passWord==null||confirmedPassWord==null){
            map.put("error_msg","密码不能为空");
            return map;
        }
        userName=userName.trim();
        if(userName.length()==0){
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
        String photo="https://cdn.acwing.com/media/user/profile/photo/172364_lg_9a3323332b.jpg";
        User user=new User(null,userName,encodedPassWord,photo,1500);

        userMapper.insert(user);

        map.put("error_msg","success");
        return map;
    }
}
