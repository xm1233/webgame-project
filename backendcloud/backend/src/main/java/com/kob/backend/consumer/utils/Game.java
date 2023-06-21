package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.dao.RecordMapper;
import com.kob.backend.pojo.Record;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    private final int[][] g;
    private final int[][] d={{1,0},{-1,0},{0,-1},{0,1}};
    private final Player playerA,playerB;
    private Integer nextStepA=null;
    private Integer nextStepB=null;
    private ReentrantLock lock=new ReentrantLock();
    private String status="playing";// playing正在游戏,finished结束游戏
    private String loser="";// "all"平局,"a"a输掉游戏,"b"b输掉游戏

    public Game(Integer rows,Integer cols,Integer inner_walls_count,Integer idA,Integer idB){
        this.cols=cols;
        this.rows=rows;
        this.inner_walls_count=inner_walls_count;
        this.g=new int[rows][cols];
        this.playerA = new Player(idA, rows-2,1,new ArrayList<>());
        this.playerB = new Player(idB,1, cols-2,new ArrayList<>());
    }

    public int[][] getG(){
        return g;
    }
    public Player getPlayerA(){return playerA;}
    public Player getPlayerB(){
        return playerB;
    }
    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            this.nextStepA=nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB=nextStepB;
        } finally {
            lock.unlock();
        }
    }


    private Boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx==tx&&sy==ty)return true;
        g[sx][sy]=1;
        for (int i = 0; i < 4; i++) {
            int x=sx+d[i][0],y=sy+d[i][1];
            if(x>=0&&x<this.rows&&y>=0&&y<this.cols&&g[x][y]==0){
                if(check_connectivity(x,y,tx,ty)){
                    g[sx][sy]=0;
                    return true;
                }
            }
        }
        g[sx][sy]=0;
        return false;
    }

    private Boolean draw(){
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if(i==0||i==this.rows-1)g[i][j]=1;
                else if(j==0||j==this.cols-1)g[i][j]=1;
                else g[i][j]=0;
            }
        }

        Random random=new Random();
        //加随机障碍物
        for (int i = 0; i < this.inner_walls_count/2; i++) {
            for(int j=0;j<1000;j++){
                int c=random.nextInt(this.cols);
                int r=random.nextInt(this.rows);
                if(g[r][c]==1||g[this.rows-1-r][this.cols-1-c]==1)continue;
                if(r==this.rows-2&&c==1)continue;
                if(r==1&&c==this.cols-2)continue;
                g[this.rows-1-r][this.cols-1-c]=g[r][c]=1;
                break;
            }
        }

        if(check_connectivity(this.rows-2,1,1,this.cols-2))return true;
        return false;

    }
    
    public void createMap(){
        for (int i = 0; i < 1000; i++) {
            if(draw())break;
        }
    }

    private boolean nextStep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA!=null&&nextStepB!=null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }
                finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //向每名玩家广播信息
    private void sendAllMessage(String message){
        if(WebSocketServer.users.get(playerA.getId())!=null){
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if(WebSocketServer.users.get(playerB.getId())!=null){
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n=cellsA.size();
        Cell cell=cellsA.get(n-1);
        if(g[cell.x][cell.y]==1)return false;
        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).x==cell.x&&cellsA.get(i).y==cell.y)return false;
        }
        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).x==cell.x&&cellsB.get(i).y==cell.y)return false;
        }
        return true;
    }

    //判断两名玩家下一步操作是否合法
    private void judge(){
        List<Cell> cellsA=playerA.getCells();
        List<Cell> cellsB=playerB.getCells();
        boolean validA=check_valid(cellsA,cellsB);
        boolean validB=check_valid(cellsB,cellsA);

        if(!validA||!validB){
            status="finished";
            if(!validA&&!validB)loser="all";
            else if(!validA)loser="a";
            else loser="b";
        }

    }

    //向两名玩家返回操作信息
    private void sendMove(){
        lock.lock();
        try {
            JSONObject resp=new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA=nextStepB=null;
        }finally {
            lock.unlock();
        }
    }

    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }


    private void saveDataBase(){
        Record record=new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                this.getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    //发送结果信息
    private void sendResult(){
        saveDataBase();
        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        sendAllMessage(resp.toJSONString());

    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            //是否获取到下一步操作
            if(nextStep()){
                judge();
                if("playing".equals(status)){
                    sendMove();
                }
                else{
                    sendResult();
                    break;
                }
            }
            else{
                status="finished";
                lock.lock();
                try {
                    if(nextStepA==null&&nextStepB==null){
                        loser="all";
                    }
                    else if(nextStepA==null){
                        loser="a";
                    }
                    else{
                        loser="b";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
