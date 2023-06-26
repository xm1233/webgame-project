package com.kob.backend.controller.pk;


import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Objects;

@RestController
@RequestMapping("/pk/")
public class ReceiveBotMoveController {

    @Resource
    private ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String,String> data){
        Integer userId=Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer direction=Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveService.receiveBotMove(userId,direction);
    }
}
