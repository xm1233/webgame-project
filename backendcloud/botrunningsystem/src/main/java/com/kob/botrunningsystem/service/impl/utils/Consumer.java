package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;

import java.util.UUID;

public class Consumer extends Thread{

    private Bot bot;
    public void startTimeOut(long timeout,Bot bot){
        this.bot=bot;
        this.start();

        try {
            this.join(timeout); //最多等待timeout秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt(); //中断当前线程
        }
    }

    private String addUid(String code,String uid){
        int k=code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k)+uid+code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid=UUID.randomUUID();
        String uid=uuid.toString().substring(0,8);

        BotInterface botInterface= Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot"+uid,
                addUid(bot.getBotCode(),uid)
        ).create().get();

        Integer direction=botInterface.nextMove(bot.getInput());

        System.out.println("move-direction: "+bot.getUserId()+" "+direction);
    }
}
