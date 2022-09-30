package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientFrame extends JFrame{
    int port = 3124;
    int id;
    int serverTime;
    InetAddress ip;
    DataInputStream dis;
    DataOutputStream dos;
    Socket cs;
    Gson convert = new Gson();


    private JPanel mainPanel;
    private JTextArea textArea1;
    private JTextField сообщениеTextField;
    private JButton отправитьButton;
    private JButton подключисьButton;
    private JLabel label;
    private JLabel ServerTime;

    public ClientFrame() {
        setContentPane(mainPanel);
        setTitle("Client");
        setSize(450, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        подключисьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ip = InetAddress.getLocalHost();
                    cs = new Socket(ip, port);
                    dos = new DataOutputStream(cs.getOutputStream());
                    Thread t = new Thread(
                            ()->{
                                try {
                                    dis = new DataInputStream(cs.getInputStream());
                                    String obj = dis.readUTF();
                                    //
                                    Events events;
                                    events = convert.fromJson(obj, Events.class);
                                    id = events.getId();
                                    serverTime = events.getTime();

                                    Thread timer = new Thread(
                                            ()->{
                                                try {
                                                    while(true){
                                                        Thread.sleep(1000);
                                                        serverTime++;
                                                        ServerTime.setText("Время сервера : " + String.valueOf(serverTime));
                                                    }
                                                } catch (InterruptedException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            }
                                    );
                                    timer.start();
                                    //

                                    label.setText("Есть подключение к серверу (" + id + ")");

                                    addEvents(events.getEvents());
                                    while(true){
                                        obj = dis.readUTF();

                                        events = convert.fromJson(obj, Events.class);

                                        String s = "";
                                        for(Integer i : events.getEvents()){
                                            if(i >= 0) {
                                                s += i + "\n";
                                            }
                                            else{
                                                s += "Событие " + (-1*i) + " выполнено\n";
                                            }
                                        }
                                        String tmp = textArea1.getText();
                                        tmp += s;
                                        textArea1.setText(tmp);
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                    );
                    t.start();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        отправитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dos!=null){
                    Events event = new Events();
                    event.setId(id);
                    event.getEvents().add(Integer.parseInt(сообщениеTextField.getText()));
                    String str = convert.toJson(event);
                    try {
                        dos.writeUTF(str);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }


    void addEvents(ArrayList<Integer> e){
        String s = "";
        for(Integer i : e){
            if(i >= 0) {
                s += i + "\n";
            }
            else{
                s += "Событие " + (-1*i) + " выполнено\n";
            }
        }
        String tmp = textArea1.getText();
        tmp += s;
        textArea1.setText(tmp);
    }


    public static void main(String[] args) {
        ClientFrame myFrame = new ClientFrame();
    }
}
