package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/bot/")
public class RemoveController {

    @Resource
    private RemoveService removeService;

    @PostMapping("remove/")
    public Map<String,String> removeBot(@RequestParam Map<String,String> data){
        return removeService.remove(data);
    }
}
