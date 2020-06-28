/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei Cheng
 */
public class ConnectionManager extends Thread{
    private static final Logger LOG = LoggerFactory.getLogger(EchoClient2.class);
    private static final String MSG_HEARTBEAT = "HBeat77";
    // initialize socket and input output streams  
    private DataInputStream  input   = null; 
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private heartbeat hbThread;
    private IncomingControl commandInThread;
    private ClientInput inputThread;
    private String address;
    private int port;
    
    
    public ConnectionManager(String address, int port){
        System.out.println("ConnectionManager: "+Thread.currentThread().getId()+" "+Thread.currentThread().getName());
        
        this.address = address;
        this.port = port;
        startConnection(address, port);
    }
    
    public void run(){
        while (true){
            if (clientSocket == null){
                startConnection(address, port);
            } else if (clientSocket.isClosed()){
                startConnection(address, port);
            } else {
                try{
                    String reply = in.readLine();
                    if (reply.equals("")) continue;
                    if (reply == null) continue;
                    if (MSG_HEARTBEAT.equals(reply)) continue;
                    if (reply.startsWith("CMD")){
                        commandInThread.processCommand(reply.substring(3));
                    }
                    

                } 
                catch(IOException i){ 
                    System.out.println("disconnected: lost connection to server. reconnecting");
                    stopConnection();
                    hbThread.remove();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        System.out.println("EchoClient2 Sleep error");
                    }
                    startConnection(address, port);
                }
            }
            
        }
        // takes input from terminal 
        //input  = new DataInputStream(System.in); 
 
        // close the connection 
        //stopConnection();
    }
    
    public boolean startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientSocket.setKeepAlive(true);
            clientSocket.setTcpNoDelay(true);
            //clientSocket.setSoTimeout(timeoutInterval);
            if (!clientSocket.isClosed()){
                System.out.println("Connected");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                hbThread = new heartbeat(clientSocket,ip,port);
                commandInThread = new IncomingControl(clientSocket);
                hbThread.setName("heartbeat");
                commandInThread.setName("IncomingControl");
                hbThread.start();
                commandInThread.start();
                return true;
            }
        } catch (IOException e) {
            LOG.debug("Error when initializing connection", e);
        }
        return false;
    }
    
    public void stopConnection() {
        try {
            in.close();
            //out.close();
            clientSocket.close();
        } catch (IOException e) {
            LOG.debug("error when closing", e);
        }
    }
}
