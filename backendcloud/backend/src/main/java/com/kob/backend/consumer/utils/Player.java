package com.kob.backend.consumer.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;
    private boolean check_tails_increasing(int step){
        if(step<=10)return true;
        return step%3==1;
    }

    public List<Cell> getCells(){
        List<Cell> res=new ArrayList<>();
        int x=sx,y=sy;
        int step=0;
        res.add(new Cell(x,y));
        int[] dx={-1,0,1,0};
        int[] dy={0,1,0,-1};
        for(int d:steps){
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            if(!check_tails_increasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }


    public String getStepsString(){
        StringBuilder res=new StringBuilder();
        for(int d:steps){
            res.append(d);
        }
        return res.toString();
    }
}
