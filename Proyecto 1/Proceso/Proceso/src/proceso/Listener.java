/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Listener extends Thread{
    
    private Client c;
    private int puerto;
    
    public Listener(Client c) {
        this.c = c;
        this.puerto = c.getPuerto();
    }
    
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(this.puerto+1000);
//            for (int i = 0; i < crr; i++) { // con esto lo que se quiere decir es que se van a esperar conexiones hasta que se llegue a la cantidad de rondas a realizar
                                              // pero esto no funciona, si tiene esto los procesos finalizan pero se muestran conexiones fallidas.
                                              // consecuencia los procesos siguen esperando por mas conexiones una vez se terminan las rondas
            while (true) {
                Socket s = ss.accept();
                // Creamos un objeto (hilo) que maneja la comunicacion del socket, 
                // ademas de modificar los atributos del proceso creador "cliente"
                ManejadorListener ml = new ManejadorListener(s, this.c);
                // el hilo inicia a trabajar
                ml.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
