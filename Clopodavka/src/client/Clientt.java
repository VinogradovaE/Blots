/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kathryn
 */
public class Clientt {
    
    int n = 5;
    char[][] field = {{'*', '-', '-', '-', '-'}, 
                          {'-', '-', '-', '-', '-'}, 
                          {'-', '-', 'x', '-', '-'}, 
                          {'-', 'x', 'x', '-', '-'}, 
                          {'-', '-', '-', '-', '-'}};
    Boolean[][] marks = {{false, false, false, false, false},
                         {false, false, false, false, false},
                         {false, false, false, false, false},
                         {false, false, false, false, false},
                         {false, false, false, false, false}};
    
    public void set(int x, int y) {
        if (!marks[x][y]) {
            marks[x][y] = true;
            for (int i = -1; i < 2; i++) 
                for (int j = -1; j < 2; j++) {
                    if (field[x+i][y+j] == 'x')
                        set(x+i, y+j);
                    else
                        field[x+i][y+j] = '*';
                }
            
        }
    }
    
    public static void main(String[] args) {
        Clientt t = new Clientt();
        // PRINT
        for (int i = 0; i < t.n; i++) {
            for (int j = 0; j < t.n; j++)
                System.out.print(t.field[i][j] + " ");
            System.out.println("");
        }
        // CLEAR
        for (int i = 0; i < t.n; i++) 
            for (int j = 0; j < t.n; j++) {
                if (t.field[i][j] == '*')
                    t.field[i][j] = '-';
                t.marks[i][i] = false;
            }
        // SET
        t.set(3, 1);
        // PRINT
        System.out.println("");
        for (int i = 0; i < t.n; i++) {
            for (int j = 0; j < t.n; j++)
                System.out.print(t.field[i][j] + " ");
            System.out.println("");
        }
        
        String q = "";
        
    }
}
