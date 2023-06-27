package com.kob.backend.controller.record;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.GetRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/record/")
public class RecordController {

    @Resource
    private GetRecordService getRecordService;

    @GetMapping("getlist/")
    public JSONObject getRecord(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRecordService.getRecord(page);
    }
}
