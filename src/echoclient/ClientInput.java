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
public class ClientInput extends Thread{
    private DataInputStream  input   = null; 
    private Socket clientSocket;
    private PrintWriter out;
    private boolean runForever = true;
    private long heartbeatDelayMilis = 10000;
    private String server;
    private int port;
    

    public ClientInput(Socket socket) {
        this.clientSocket = socket;
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("ClientInput error");
        }
    }

    public void run() {
        System.out.println("started input thread");
        
        // takes input from terminal 
        input  = new DataInputStream(System.in); 
  
        // string to read message from input 
        String line = ""; 
  
        // keep reading until "Over" is input 
        while (runForever) 
        { 
            try
            { 
                line = input.readLine();
                out.println(line);
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        }
        System.out.println("end of input thread");
    }
    
    public void remove(){
        runForever = false;
        out.close();
        try {
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
