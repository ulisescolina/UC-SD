/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientelogs;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.comun.LibComun;
/**
 *
 * @author netbeans
 */
public class Cliente{
    // Iterador que contiene los servidores del archivo que lista los servidores
    Iterator<Map.Entry<String, String[]>> servers;
    // Coleccion de hilos que consultan concurrentemente con los servidores
    ArrayList<ManejadorCliente> hilosConsulta = new ArrayList<>();
    
    public Cliente(Iterator<Map.Entry<String, String[]>> hosts)
    {
        this.servers = hosts;
    }
    
    public void consultar(String buscar) throws InterruptedException
    {
        String ip = null;
        int puerto = 0;
        // Socket para la conexion con el servidor
        Socket s = null;
        // Bucle que envia las peticiones a todos los clientes que se declaran
        // en el archivo de servidores
        while (servers.hasNext()) {
            Map.Entry<String, String[]> elemento = servers.next();
            try {
                ip = elemento.getValue()[0];
                puerto = Integer.parseInt(elemento.getValue()[1]);
                s = new Socket(ip, puerto);
                ManejadorCliente mc = new ManejadorCliente(s, buscar);
                mc.start();
                this.hilosConsulta.add(mc);
            } catch (java.net.ConnectException e) {
                System.err.println("No se pudo establecer la conexion con el servidor de log alojado en "+ip+":"+String.valueOf(puerto));
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /*Programar la logica que se encargue de esperar por los hilos*/
        Iterator<ManejadorCliente> hilos = this.hilosConsulta.iterator();
        while (hilos.hasNext()) {
            hilos.next().join();
        }
    }
}
