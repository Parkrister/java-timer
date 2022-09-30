package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Events {
    ArrayList<Integer> events = new ArrayList<>();
    int id = -1;
    MyTimer serverTime = new MyTimer();

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Integer> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return serverTime.getTime();
    }

    public void setTime(int time) {
        this.serverTime.setTime(time);
    }

    public Events(){}

    public Events(Integer id, Integer t, Integer event){
        this.id = id;
        serverTime.setTime(t);
        this.events.add(event);
    }

    @Override
    public String toString() {
        String s = "";
        s += "Message{" + "msg=";
        for(Integer integer : events){
            s += integer + "\n";
        }
        return s + ", id=" + id + ')';
    }
}
