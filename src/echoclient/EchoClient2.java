/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.logging.Level;

/**
 *
 * @author Wei Cheng
 */
public class EchoClient2 {
    private static final Logger LOG = LoggerFactory.getLogger(EchoClient2.class);
    private static final String MSG_HEARTBEAT = "HBeat77";
    
    // initialize socket and input output streams  
    private DataInputStream  input   = null; 
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private heartbeat hbThread;
    private ClientInput inputThread;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //EchoClient2 client = new EchoClient2("127.0.0.1", 5555); 
        ConnectionManager client = new ConnectionManager("127.0.0.1", 5555);
        client.setName("ConnectionManager");
        client.start();
    }
    
    // constructor to put ip address and port 
    public EchoClient2(String address, int port) 
    { 
        
         startConnection(address, port);

        // takes input from terminal 
        //input  = new DataInputStream(System.in); 
  
        // string to read message from input 
        String line = ""; 
  
        // keep reading until "Over" is input 
        while (!line.equals("Over")) 
        { 
            try
            { 
                String reply = in.readLine();
                if (reply.equals("")) continue;
                if (reply == null) continue;
                //if (MSG_HEARTBEAT.equals(reply)) continue;
                switch(reply){
                    case "one":
                        System.out.println("one");
                        break;
                    case "two":
                        System.out.println("two");
                        break;
                    case "2":
                        System.out.println("2");
                        break;
                    case "id":
                        System.out.println("my system id is xxx \n ok then");
                        sendMessage2("my system id is xxx");
                        break;
                    default:
                        System.out.println("default server reply: "+reply);
                }
                
            } 
            catch(IOException i){ 
                System.out.println("disconnected: lost connection to server. reconnecting : isConnected: "+clientSocket.isConnected());
                System.out.println("disconnected: isClosed main: "+clientSocket.isClosed());
                stopConnection();
                System.out.println("disconnected: isClosed main: "+clientSocket.isClosed());
                System.out.println("disconnected: isConnected: "+clientSocket.isConnected());
                hbThread.remove();
                inputThread.remove();
                clearBuffer();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    System.out.println("EchoClient2 Sleep error");
                }
                startConnection(address, port);
                
            } 
        } 
  
        // close the connection 
        stopConnection();
    }
    
    public boolean startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientSocket.setKeepAlive(true);
            clientSocket.setTcpNoDelay(true);
            //clientSocket.setSoTimeout(timeoutInterval);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if (!clientSocket.isClosed()){
                System.out.println("Connected");
                System.out.println("isClosed: "+clientSocket.isClosed());
                hbThread = new heartbeat(clientSocket,ip,port);
                inputThread = new ClientInput(clientSocket);
                hbThread.start();
                inputThread.start();
                return true;
            }
        } catch (IOException e) {
            LOG.debug("Error when initializing connection", e);
        }
        return false;
    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void sendMessage2(String msg) {
        out.println(msg);
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            System.out.println("disconnected: isClosed: "+clientSocket.isClosed());
            clientSocket.close();
            System.out.println("disconnected: isClosed: "+clientSocket.isClosed());
        } catch (IOException e) {
            LOG.debug("error when closing", e);
        }
    }
    
    public void clearBuffer() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            LOG.debug("error when closing", e);
        }
    }
    
}
