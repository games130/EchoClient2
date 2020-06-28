/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Wei Cheng
 */
public class heartbeat extends Thread{
    private Socket clientSocket;
    private PrintWriter out;
    private boolean runForever = true;
    private long heartbeatDelayMilis = 10000;
    private String server;
    private int port;
    

    public heartbeat(Socket socket, String server, int port) {
        System.out.println("heartbeat: "+Thread.currentThread().getId()+" "+Thread.currentThread().getName());
        this.clientSocket = socket;
        this.server = server;
        this.port = port;
    }

    public void run() {
        System.out.println("started heartbeat");
        
        while (runForever) {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("HBeat77");
                System.out.println("send HB");
                Thread.sleep(heartbeatDelayMilis);
            } catch (IOException e) {
                System.out.println("disconnected: lost connection to server. reconnecting");
                connect();
            } catch (InterruptedException ex) {
                runForever = false;
            }
        }
        out.close();
        /*try {
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("HB closing error");
        }*/
    }
    
    private void connect(){
        try {
            clientSocket = new Socket(server, port);
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }
    
    public void remove(){
        runForever = false;
    }
    
    public void processHeartbeat(){
        
    }
    
    
}
