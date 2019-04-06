/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Kathryn
 */
public class Server extends Thread {
    
    ArrayList<ClientThread> players = new ArrayList<>();
    
    int port = 4123;
    InetAddress ip = null;
    ServerSocket ss;

    public Server() {
        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 0, ip);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Server created");
    }
    
    @Override
    public void run() {
        try {
           System.out.println("Server started");
           while(players.size() < 2) {
               Socket cs = ss.accept();
               ClientThread ct = new ClientThread(cs, this);
               players.add(ct);
               System.out.println("Client connected");
               ct.start();
               
           }
        } catch (IOException ex) {
           System.out.println("Server stopped");
        }   
    }
    
    public void sendToAll(String m, ClientThread ct) {
        for (ClientThread player : players) {
            if (player != ct)
                player.sendMsg(m);
        }
    }
    
    public void sendStatus(int ind, ClientThread ct) {
        ct.sendStat(ind);
    }
    
    public static void main(String args[]) {
        Server st = new Server();
        st.start();
    }
}
