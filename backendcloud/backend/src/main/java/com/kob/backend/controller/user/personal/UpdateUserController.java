package com.kob.backend.controller.user.personal;


import com.kob.backend.service.user.personal.UpdateUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/personal/")
public class UpdateUserController {

    @Resource
    private UpdateUserService updateUserService;

    @PostMapping("update/")
    public Map<String,String> updateUser(@RequestParam Map<String,String>data){
        return updateUserService.updateUser(data);
    }
}
