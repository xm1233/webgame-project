package com.kob.backend.service.impl.record;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.dao.RecordMapper;
import com.kob.backend.dao.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordServiceImpl implements GetRecordService {
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getRecord(Integer page) {
        IPage<Record> recordIPage=new Page<>(page,40);
        QueryWrapper<Record> recordQueryWrapper=new QueryWrapper<>();
        recordQueryWrapper.orderByDesc("id");
        List<Record> recordList = recordMapper.selectPage(recordIPage,recordQueryWrapper).getRecords();
        JSONObject resp=new JSONObject();
        List<JSONObject> items=new LinkedList<>();
        for(Record record:recordList){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item=new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("b_photo",userB.getPhoto());
            item.put("a_username",userA.getUserName());
            item.put("b_username",userB.getUserName());
            String result="平局";
            if("a".equals(record.getLoser()))result="玩家B获胜";
            else if("b".equals(record.getLoser()))result="玩家A获胜";
            item.put("result",result);
            item.put("record",record);
            items.add(item);
        }

        resp.put("records",items);
        resp.put("records_count",recordMapper.selectCount(null));


        return resp;
    }
}
