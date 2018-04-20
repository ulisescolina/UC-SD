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
public class Cliente extends Thread{
    HashMap<String, String[]> hosts = new HashMap<>();
    
    public Cliente()
    {
        this.hosts = (new LibComun()).getHosts("hosts.txt");
    }
    
    public void consultar(String buscar) throws InterruptedException
    {
        try {
            Socket s = new Socket("localhost", 7001);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(buscar);
            
            try {
                // Creamos el flujo para recibir lo que manda el servidor
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                // Creamos la coleccion para recibir los datos del socket 
                ArrayList<String> resultado = (ArrayList<String>) ois.readObject();
                Iterator<String> it = resultado.iterator();
                // recorremos el iterador imprimimos los elementos, que corresponden a una linea 
                while (it.hasNext()) {
                    System.out.println(it.next());
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
