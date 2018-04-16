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
    private int puerto, controlEnvios = 0, controlRecepciones = 0, sumaEnvios = 0, sumaRecepciones = 0;
    private String ip;
    // Parametros 
    private int cantRondasARealizar = 5000, mensajesPorRonda =5;
    
    int port = 7003;
    
    public Client (int p) throws UnknownHostException {
        this.puerto = p;
        this.ip = Inet4Address.getLocalHost().getHostAddress();
    }
    
    public Client(int p, int mpr, int crr){
        this.puerto = p;
        this.mensajesPorRonda = mpr;
        this.cantRondasARealizar = crr;
    }
    
    public void conectar() {
        try {
            // Creamos el proceso que se encarga de recibir las conexiones TCP de
            // los otros procesos que estan levantados
            Listener listener = new Listener(this); // se le pasa el objeto Client actual para poder acceder a los metodos y asi modificar los atributos
            listener.start();
            
            // Establecemos la conexion al Servidor (Clasificador de Salidas)
            Socket s = new Socket("localhost", 7000);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // informamos que ql cliente esta levantado
            dos.writeUTF("UP: "+ String.valueOf(port)); // una vez finalizadas las pruebas esto puede desaparecer.
//             dos.writeUTF("UP"+ String.valueOf(this.getPuerto()));
            
            // Esperamos el mensaje para iniciar a trabajar
            ServerSocket ss = new ServerSocket(port); // una vez finalizadas las pruebas esto puede desaparecer.
            //ServerSocket ss = new ServerSocket(this.getPuerto()); 
            Socket sEscucha = ss.accept();
            DataInputStream dis = new DataInputStream(sEscucha.getInputStream());
            String sign = dis.readUTF();
            System.out.println(sign);
            if ("START".equals(sign)) { // esto es trivial pero lo agrego, en un futuro nos podria servir
                 Sender sender = new Sender(this);
                 sender.start();
            }
            dos.close();
            s.close();
            dis.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getControlEnvios() {
        return controlEnvios;
    }

    /*
        Cantidad de mensajes enviados por el proceso.
    */
    public synchronized void incrementarControlEnvios() {
        this.controlEnvios++;
    }

    
    public int getControlRecepciones() {
        return controlRecepciones;
    }

    /*
        Cantidad de mensajes recibidos por el proceso.
    */
    public synchronized void incrementarControlRecepciones() {
        this.controlRecepciones++;
    }

    public int getSumaEnvios() {
        return sumaEnvios;
    }
    
    /*
        Suma de los envios que realizo el proceso.
    */
    public synchronized void incrementarSumaEnvios(int sumaEnvios) {
        this.sumaEnvios += sumaEnvios;
    }

    public int getSumaRecepciones() {
        return sumaRecepciones;
    }

    /*
        Suma de enteros que fueron recibidos por los procesos.
    */
    public synchronized void incrementarSumaRecepciones(int sumaRecepciones) {
        this.sumaRecepciones += sumaRecepciones;
    }

    public int getCantRondasARealizar() {
        return cantRondasARealizar;
    }

    public int getMensajesPorRonda() {
        return mensajesPorRonda;
    }
    
    public int getPuerto() {
        return this.port;
    }
    
    public String getIP() {
        return this.ip;
    }
}
