/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Client {
    int puerto, controlEnvios = 0, controlRecepciones = 0, sumaEnvios = 0, sumaRecepciones = 0;
    // Parametros 
    int cantRondasARealizar = 5000, mensajesPorRonda =5;
    
    public Client (int p) {
        this.puerto = p;
    }
    
    public Client(int p, int mpr, int crr){
        this.puerto = p;
        this.mensajesPorRonda = mpr;
        this.cantRondasARealizar = crr;
    }
    
    public void conectar() {
        try {
            int port = 7001;
            // Creamos el proceso que se encarga de recibir las conexiones TCP de
            // los otros procesos que estan levantados
            Listener listener = new Listener();
            listener.start();
            
            // Establecemos la conexion al Servidor (Clasificador de Salidas)
            Socket s = new Socket("localhost", 7000);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // informamos que ql cliente esta levantado
            dos.writeUTF("UP: "+ String.valueOf(port)); // una vez finalizadas las pruebas esto puede desaparecer.
            // dos.writeUTF("UP"+ String.valueOf(this.puerto));
            
            // Esperamos el mensaje para iniciar a trabajar
            ServerSocket ss = new ServerSocket(port); // una vez finalizadas las pruebas esto puede desaparecer.
            //ServerSocket ss = new ServerSocket(this.puerto); 
            Socket sEscucha = ss.accept();
            DataInputStream dis = new DataInputStream(sEscucha.getInputStream());
            String sign = dis.readUTF();
            System.out.println(sign);
            if ("START".equals(sign)) { // esto es trivial pero lo agrego, en un futuro nos podria servir
                 Sender sender = new Sender(this.mensajesPorRonda, this.cantRondasARealizar);
                 sender.start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
