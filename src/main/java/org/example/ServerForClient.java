package org.example;

import com.google.gson.Gson;
import com.sun.tools.javac.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerForClient {
    int id;
    int serverTime;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    Gson convert = new Gson();
    MainServer ms;

    Thread t;

    public ServerForClient(int id, Socket cs, MainServer ms, int serverTime) throws  IOException{
        this.id = id;
        this.cs = cs;
        this.ms = ms;
        this.serverTime = serverTime;

        System.out.println("Подключился клиент " + id + ", время " + serverTime + '\n');

        try {
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        t = new Thread(
                ()->{
                    try {
                        dis = new DataInputStream(cs.getInputStream());
                        while(true){
                            String obj;
                            obj = dis.readUTF();
                            //
                             Events events = convert.fromJson(obj, Events.class);
                             System.out.println("Получил " + events);
                             for(Integer integer : events.getEvents()){
                                 ms.addEvent(integer);
                             }
                            //
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        t.start();
        sendID();
    }

    void sendID(){
        Events event = new Events();
        event.setId(id);
        event.setTime(serverTime);
        event.getEvents().addAll(ms.allEvents);

        String sendStr = convert.toJson(event);
        try {
            dos.writeUTF(sendStr);
            System.out.println("Отправил ID " + event.getId() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendEvent(Integer e){
        Events event = new Events();
        event.getEvents().add(e);

        String sendStr = convert.toJson(event);
        try {
            dos.writeUTF(sendStr);
            System.out.println("Отправил время события " + event);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    void removeEvent(Integer e){}

    /*
    void sendMsg(String s){
        Message msg = new Message();
        msg.getMsg().add(s);
        String sendStr = convert.toJson(msg);
        try {
            dos.writeUTF(sendStr);
            System.out.println("Отправил строку " + msg + " клиенту (" + id + ")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void sendAllMsg(ArrayList<String> s){

    }
    */

}
