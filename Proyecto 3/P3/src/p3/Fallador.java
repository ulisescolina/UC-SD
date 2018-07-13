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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author urc
 */
public class Fallador extends Thread{
    private Proceso p;

    public Fallador(Proceso p) {
        this.p = p;
    }
    
    @Override
    public void run(){
        try {
//            Thread.sleep(1000);
            while (true) {
                this.chkProcesosActivos();
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Fallador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void chkProcesosActivos() {
        if (!this.p.getVecinos().isEmpty() && !this.p.getTemporizadores().isEmpty()) {
            Iterator<Entry<Integer, Proceso>> vecinos = this.p.getVecinos().entrySet().iterator();
            while (vecinos.hasNext()) {
                Proceso vecino = vecinos.next().getValue();
                // Se recupera el temporizador del proceso en cuestion
                long tVecino = this.p.getTemporizadores().get(vecino.getId());
                // Se obtiene la hora del SO
                long tSistema = System.currentTimeMillis();
                long diff = tSistema - tVecino;
                if (diff > this.p.getToleranciaVidaProceso()+500) {
                    // Lo quito de la tabla de procesos
                    this.p.quitarProceso(vecino);
                    // Lo quito de la tabla de vecinos
                    this.p.quitarVecino(vecino);
                    // Actualizo la tabla de vecinos
                    this.p.actualizarVecinos();
                    // Comunico a los vecinos restantes que se ha muerto un proceso
                    this.comunicarMuerteProceso(vecino);
                    System.err.println("EL PROCESO "+vecino.getId()+" FALLO, tS-tV = "+diff+"\n");
                }
            }
        }
    }

    /**
    * Envia a los vecinos un nuevo Proceso a ser agregado a la lista de procesos
    * @param p proceso que se envia a los vecinos
    */
    private void comunicarMuerteProceso(Proceso p) {
        HashMap<String, Proceso> mensaje = new HashMap<>();
        Iterator<Entry<Integer, Proceso>> it = this.p.getVecinos().entrySet().iterator();
        // Armar mensaje a ser enviado
        mensaje.put("DEATH", p);
        while (it.hasNext()) {
            Proceso vecino = it.next().getValue();
            this.enviarProceso(mensaje, vecino.getIpPropia(), vecino.getPuertoPropio());
        }
    }
    
    public void enviarProceso(HashMap<String, Proceso> hmp, InetAddress ip, int puerto){
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(hmp);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, puerto);
            ds.send(sendPacket);
            ds.close();
            os.close();
        } catch (SocketException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
