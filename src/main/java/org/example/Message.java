package org.example;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
    ArrayList<String> msg = new ArrayList<>();
    int id = -1;

    public ArrayList<String> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<String> msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Message() {}

    public Message(int id, String msg) {
        this.id = id;
        this.msg.add(msg);
    }

    @Override
    public String toString() {
        String s = "";
        s += "Message{" + "msg=";
        for(String string : msg){
            s += string + "\n";
        }
        return s + ", id=" + id + ')';
    }

}