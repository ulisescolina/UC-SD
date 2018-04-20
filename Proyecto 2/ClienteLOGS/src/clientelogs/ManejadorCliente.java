/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientelogs;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class ManejadorCliente extends Thread{
    private final Socket s;
    private final String cadenaBuscar;
    
    public ManejadorCliente(Socket s, String cadenaBuscar)
    {
        this.s = s;
        this.cadenaBuscar = cadenaBuscar;
    }
    
    @Override
    public void run() 
    {   
        try {
            // Creamos el flujo por el cual enviamos la consulta al servidor
            DataOutputStream dos = new DataOutputStream(this.s.getOutputStream());
            // mandamos la cadena a buscar al servidor
            dos.writeUTF(cadenaBuscar);
            
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
        } catch (IOException ex) {
            Logger.getLogger(ManejadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
