package com.kob.backend.controller.user.account;

import com.kob.backend.service.impl.user.account.InfoServiceImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account/")
public class InfoController {

    @Resource
    private InfoService infoService;

    @GetMapping("info/")
    public Map<String,String> getInfo(){
        return infoService.getInfo();
    }
}
