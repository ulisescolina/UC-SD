/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto01;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seven
 */
public class Proceso extends Thread{
    
    private int controlEnvios, controlRecepciones, sumaEnvios, sumaRecepciones;
    private String host, serverIP;
    private int puerto, serverPort;
    
    public Proceso (String[] host, String[] server){
        this.setHost(host[0]);
        this.setPuerto(Integer.parseInt(host[1]));
        this.setServerIP(server[0]);
        this.setServerPort(Integer.parseInt(server[1]));
    }

    @Override
    public void run (){
        System.out.println(host + ":" + puerto);
        try {
            Socket nucleo = new Socket(this.serverIP, this.serverPort);
            // Enviar un mensaje por TCP para comprobar funcionamiento
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public int getControlEnvios() {
        return controlEnvios;
    }

    public void setControlEnvios(int controlEnvios) {
        this.controlEnvios = controlEnvios;
    }

    public int getControlRecepciones() {
        return controlRecepciones;
    }

    public void setControlRecepciones(int controlRecepciones) {
        this.controlRecepciones = controlRecepciones;
    }

    public int getSumaEnvios() {
        return sumaEnvios;
    }

    public void setSumaEnvios(int sumaEnvios) {
        this.sumaEnvios = sumaEnvios;
    }

    public int getSumaRecepciones() {
        return sumaRecepciones;
    }

    public void setSumaRecepciones(int sumaRecepciones) {
        this.sumaRecepciones = sumaRecepciones;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    
    
}
