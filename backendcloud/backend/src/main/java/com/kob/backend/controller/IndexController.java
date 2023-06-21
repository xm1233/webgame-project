package com.kob.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class IndexController {

    @RequestMapping("index/")
    public String index(){
        return "pk/index.html";
    }

    @GetMapping("bot")
    public Map<String,String> bot(){
        Map<String,String> map=new HashMap<>();
        map.put("name","jack");
        map.put("damage","1000");
        return map;
    }


}
