/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author urc
 */
public class MarcaPaso extends Thread{
    private Proceso p;

    public MarcaPaso(Proceso p) {
        this.p = p;
    }

    @Override
    public void run() {
        try {
            while (true) {
                    this.comunicarEstadoProcesoVecinos(this.p);
                    Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(MarcaPaso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Envia a los vecinos un nuevo Proceso a ser agregado a la lista de procesos
     * @param p proceso que se envia a los vecinos
     */
    public void comunicarEstadoProcesoVecinos(Proceso p){
        if (!p.getVecinos().isEmpty()) {
            HashMap<String, Proceso> mensaje = new HashMap<>();
            Iterator<Map.Entry<Integer, Proceso>> it = p.getVecinos().entrySet().iterator();
            // Armar mensaje a ser enviado
            mensaje.put("LIVE", p);
            while (it.hasNext()) {
                Proceso vecino = it.next().getValue();
                synchronized (mensaje) {
                    this.enviarProceso(mensaje, vecino.getIpPropia(), vecino.getPuertoPropio());
                    P3.getLog().log(Level.INFO, "HeartBeat desde el proceso {0}, al proceso {1}", new Object[]{p.getId(), vecino.getId()});
                }
                // System.out.println("Enviando mensaje: "+ mensaje+", al Proceso: "+vecino);
            }
        }
    }
    
    public synchronized void enviarProceso(HashMap<String, Proceso> hmp, InetAddress ip, int puerto){
        DatagramSocket ds = null;
        ObjectOutputStream os = null;
        try {
            ds = new DatagramSocket();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(outputStream);
            os.writeObject(hmp);
            os.flush();
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, puerto);
            ds.send(sendPacket);
        } catch (SocketException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ds.close();
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(MarcaPaso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
