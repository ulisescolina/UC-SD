/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorlogs;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Nodo {
    
    String ip, nodo;
    int puerto;
    
    public Nodo(int puerto, String nodo) 
    {
        this.puerto = puerto;
        this.nodo = nodo;
    }
    
    public void iniciar() 
    {
        try {
            ServerSocket ss = new ServerSocket(this.puerto);
            while (true) {
                Socket s = ss.accept();
                ManejadorNodo mn = new ManejadorNodo(s, nodo);
                mn.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
