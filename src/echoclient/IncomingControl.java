/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

/**
 *
 * @author Wei Cheng
 */
public class IncomingControl extends Thread{
    private static final String MSG_HEARTBEAT = "HBeat77";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public IncomingControl(Socket socket) {
        this.clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("IncomingControl init out parameter error");
            java.util.logging.Logger.getLogger(IncomingControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        System.out.println("started");
        /*try {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("input: "+inputLine);
                if (inputLine.equals("")) continue;
                if (inputLine == null) continue;
                if (MSG_HEARTBEAT.equals(inputLine)) continue;
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                switch (inputLine){
                    case "id":
                        System.out.println("my system id is xxx");
                        break;
                    default:
                        System.out.println("IncomingControl unknown command received: "+inputLine);
                }

            }

            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("IncomingControl some error: "+e.getMessage());
        }*/
    }
    
    public void processCommand(String reply){
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
                sendMessage("my system id is xxx");
                break;
            case "rssh":
                //String command = "ls";
                String[] command ={"cmd", "/c","dir"};
                String result = executeCommand(command);
                System.out.println("cmd execute result: "+result);
                break;
            default:
                System.out.println("default server reply: "+reply);
        }
    }
    
    public void sendMessage(String msg) {
        out.println(msg);
    }
    
    public String executeCommand(String[] command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";           
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
    
    public String executeCommand(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";           
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    
}
