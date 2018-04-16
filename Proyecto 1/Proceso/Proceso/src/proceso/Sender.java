/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;
import lib.comun.LibComun;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Sender extends Thread {
    private final int mpr, crr;
    private final int LIM_SUP = 100;
    private Client c;
    HashMap<String, String[]> hosts = new HashMap<>();
    
    public Sender(Client c) 
    {
        this.mpr = c.getMensajesPorRonda();
        this.crr = c.getCantRondasARealizar();
        this.c = c;
        this.hosts = (new LibComun()).getHosts("hosts.txt");// instanciamos libreria que contiene metodos comunes entre proyectos, ej: obtener las lineas de hosts:puerto.
    }
    
    @Override
    public void run() {
        for (int i = 0; i < this.crr; i++) {
            realizarRonda(i);
        }
        reportarDatosProceso("localhost",6999);
    }

    private void realizarRonda(int i) 
    {
        try {
                // se puede mejorar esto
                int puertoProcesoAleatorio = (this.getProcesoAleatorio()+8000); // aca agarramos un aleatorio en el rango de 1 a la cantidad de host que haya. y le sumamos 8000, los puertos de escucha entonces serán 8001, 8002, 8003, ..., 800n.
                Socket s = new Socket("localhost", puertoProcesoAleatorio);// para una futura iteración se puede implementar un metodo que seleccione un elemento aleatorio del HashMap que contiene los datos del archivo
                System.out.println(c.getIP() +":"+puertoProcesoAleatorio);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                for (int j = 0; j < this.mpr; j++) {
                    int numeroAleatorio = Sender.getEnteroAleatorio(LIM_SUP);
                    dos.writeUTF(String.valueOf(numeroAleatorio));
                    c.incrementarControlEnvios();
                    c.incrementarSumaEnvios(numeroAleatorio);
                    System.out.println("Mensaje: "+j+", Ronda: "+i+", Valor Numero Aleatorio: "+numeroAleatorio);
                }
                // Cerramos el socket
                dos.close();
                s.close();
                
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
    }    
    
    private void reportarDatosProceso(String host, int puerto)
    {
        String datos = this.armarDatosEnviar(c.getIP(), c.getPuerto(), c.getControlEnvios(), c.getControlRecepciones(), c.getSumaEnvios(), c.getSumaRecepciones());
        try {
            Socket s = new Socket(host, puerto);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(datos);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static int getEnteroAleatorio(int limSup)
    {
        Random aleatorio = new Random(System.currentTimeMillis());
        return (aleatorio.nextInt(limSup)+1);
    }
    
    private int getProcesoAleatorio()
    {
        int proceso = Sender.getEnteroAleatorio(hosts.size());
        return proceso;
    }
    
    private String armarDatosEnviar(String host, int puerto, int msjEnviados, int msjRecepciones, int sumaEnvios, int sumaRecepciones)
    {
        return (host+":"+String.valueOf(puerto)+":"+String.valueOf(msjEnviados)+":"+String.valueOf(msjRecepciones)+":"+String.valueOf(sumaEnvios)+":"+String.valueOf(sumaRecepciones));
    }
}
