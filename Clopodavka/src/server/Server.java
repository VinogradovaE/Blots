/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Kathryn
 */
public class Server extends Thread {
    int port = 3124;
    ServerSocket ss;
    ArrayList<Presenter> players = new ArrayList<>();
    // TODO play data
    JTextArea log;
    
    public Server() {
        try {
            ss = new ServerSocket(port);
            System.out.println("Server created");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Server started");
            while(players.size() < 2) {
                Socket cs;
                cs = ss.accept();
                Presenter presenter = new Presenter(cs, this);
                players.add(presenter);
                System.out.println("Player " + players.size() + " connected");
                presenter.start();
            }
        } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void sendToAll(String m) {
        for (Presenter player: players) {
            player.sendMsg(m);
        }
    }
    
    public void addToLog(String s) {
        String t = log.getText();
        t += s + '\n';
        log.setText(t);
    }
    
    public static void main(String[] args) {
        new Server().start();
    }
}
