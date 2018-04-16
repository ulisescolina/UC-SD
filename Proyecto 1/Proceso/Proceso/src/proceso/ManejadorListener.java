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

public class ManejadorListener extends Thread{
    
    Socket comunicacionProcesos;
    Client procesoCreador;
    
    public ManejadorListener(Socket socket, Client c) {
        this.comunicacionProcesos = socket;
        this.procesoCreador = c;
    }
    
    @Override
    public void run() {
        try {
            // obtenemos el data input stream del socket que nos pasa el proceso listener.
            DataInputStream dis = new DataInputStream(comunicacionProcesos.getInputStream());
            int cmr = this.procesoCreador.getMensajesPorRonda();
            for (int i=0; i < cmr ; i++) {
                String entero = dis.readUTF();// leemos lo que nos llega en la cantidad de rondas que tengamos configuradas
                System.out.println("Entero recibido: "+entero);
                this.procesoCreador.incrementarSumaRecepciones(Integer.parseInt(entero));
                this.procesoCreador.incrementarControlRecepciones();
            }
            dis.close();
            this.comunicacionProcesos.close();
        } catch (IOException ex) {
            Logger.getLogger(ManejadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
