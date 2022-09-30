package org.example;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {
    MyTimer time = new MyTimer(0);
    int count = 0;
    int port = 3124;
    ServerSocket ss;
    InetAddress ip;
    //ArrayList<String> allMsg = new ArrayList<>();

    ArrayList<Integer> allEvents = new ArrayList<>();
    ArrayList<ServerForClient> allClient = new ArrayList<>();


    public void addEvent(Integer e){
        allEvents.add(e);
        for(ServerForClient serverForClient : allClient){
            serverForClient.sendEvent(e);
        }
    }



    public MainServer(){

        try {
            ip = InetAddress.getLocalHost();

            ServerSocket ss = new ServerSocket(port, 0, ip);
            System.out.println("Сервер запущен!");

            Thread t = new Thread(
                    ()->{
                        try {
                            while(true){
                                Thread.sleep(1000);
                                time.Tick();
                                System.out.println("--" + time.getTime() + "--");
                                ArrayList<Integer> rem = new ArrayList<>();
                                for(Integer i : allEvents){
                                        if(i == time.getTime()){
                                            System.out.println("\nНаступило событие : " + i);
                                            rem.add(i);
                                        }
                                        System.out.print(String.valueOf(i) + ' ');
                                }
                                for(Integer i : rem){
                                    allEvents.removeAll(Arrays.asList(i));
                                    addEvent(-1*i);
                                }
                                System.out.print('\n');

                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            t.start();


            while (true) {
                Socket cs = ss.accept();
                count++;
                ServerForClient sc = new ServerForClient(count, cs, this, time.getTime());
                allClient.add(sc);
            }


        } catch (IOException ex) {
            System.out.println("Не могу создать сервер");
        }
    }

    public static void main(String[] args) {
        new MainServer();
    }
}
