/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author urc
 */
public class Escuchador extends Thread{
    private Proceso p;
    
    public Escuchador (Proceso p) {
        this.p = p;
    }
    
    @Override
    public void run() {
        DatagramSocket dSocket = null;
        try {
            dSocket = new DatagramSocket(this.p.getPuertoPropio());
            while (true) {
                this.esperarMensaje(dSocket);
            }
        } catch (SocketException ex) {
            P3.getLog().log(Level.SEVERE, "Hubieron problemas de conexion con el socket.");
        } finally {
            dSocket.close();
        }       
    }

    /**
     * Recibe el mensaje mediante el socket y crea un hilo para que maneje el mismo.
     * @param dSocket socket por el cual recibe el mensaje.
     */
    private void esperarMensaje(DatagramSocket dSocket) {
        try {
            byte[] buffer = new byte[4096];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            dSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            ManejadorMensaje mm = new ManejadorMensaje(this.p, is);
            mm.start();
        } catch (IOException ex) {
            Logger.getLogger(Escuchador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
