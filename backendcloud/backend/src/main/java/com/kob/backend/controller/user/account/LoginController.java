package com.kob.backend.controller.user.account;


import com.kob.backend.service.impl.user.account.LoginServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account/")
public class LoginController {

    @Resource
    LoginServiceImpl loginService;

    @PostMapping("login/")
    public Map<String,String> getToken(@RequestParam Map<String,String>map){
        String username=map.get("username");
        String password=map.get("password");
        return loginService.login(username,password);

    }
}
