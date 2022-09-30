package org.example;

import java.util.Date;
import java.util.Timer;

public class MyTimer{

    Integer time = 0;


    public MyTimer(){}

    public MyTimer(int time){
        this.time = time;
    }

    void Tick(){
        time++;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Time: " + time + ')';
    }
}
