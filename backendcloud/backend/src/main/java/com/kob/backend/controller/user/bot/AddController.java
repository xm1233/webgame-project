package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/bot/")
public class AddController {

    @Resource
    private AddService addService;

    @PostMapping("add/")
    public Map<String,String> addBot(@RequestParam Map<String,String> map){
        return addService.add((map));
    }
}
