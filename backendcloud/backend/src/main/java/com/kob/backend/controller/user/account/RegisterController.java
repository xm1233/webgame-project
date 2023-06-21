package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account/")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @PostMapping("register/")
    public Map<String,String> register(@RequestParam Map<String,String> map){
        return registerService.register(map.get("username"),map.get("password"),map.get("confirmedPassWord"));
    }
}
