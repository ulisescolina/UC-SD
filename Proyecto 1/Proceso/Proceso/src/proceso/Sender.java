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
    private Iterator<Map.Entry<String, String[]>> it;

    
    public Sender(Client c) 
    {
        this.mpr = c.getMensajesPorRonda();
        this.crr = c.getCantRondasARealizar();
        this.c = c;
        
    }
    
    @Override
    public void run() {
        for (int i = 0; i < this.crr; i++) {
            realizarRonda(i);
        }
        finalizado();
    }
    
    private void finalizado() 
    {
        it = c.getIt();
        while (it.hasNext()) {  
                Map.Entry<String, String[]> elemento = it.next();
                /* Con elemento estoy accediendo a uno de los objetos del iterador
                getKey: devuelve la clave del elemento.
                getValue: devuelve el valor del elemento. */
                Socket s;
            try {
                s = new Socket(elemento.getValue()[0], Integer.parseInt(elemento.getValue()[1])+1000);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                // enviamos la señal de inicio.
                dos.writeUTF("FIN");
                // Cerramos la conexion del socket
                dos.close();
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
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
    
    
    private static int getEnteroAleatorio(int limSup)
    {
        Random aleatorio = new Random(System.currentTimeMillis());
        return (aleatorio.nextInt(limSup)+1);
    }
    
    private int getProcesoAleatorio()
    {
        int proceso = Sender.getEnteroAleatorio(c.getHosts().size());
        return proceso;
    }
    
}
