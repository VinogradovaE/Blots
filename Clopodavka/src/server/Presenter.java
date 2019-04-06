/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kathryn
 */
public class Presenter extends Thread {
    Socket cs;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Server server;
    
    public Presenter(Socket cs, Server s) {
        this.cs = cs;
        try {
            ois = new ObjectInputStream(cs.getInputStream());
            oos = new ObjectOutputStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.server = s;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String s = ois.readUTF();
                server.sendToAll(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public void sendMsg(String m) {
        try {
            oos.writeUTF(m);
        } catch (IOException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
