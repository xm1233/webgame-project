package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.GetListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/bot/")
public class GetListController {

    @Resource
    private GetListService getListService;

    @GetMapping("getlist/")
    public List<Bot> getList(){
        return getListService.getList();
    }
}
