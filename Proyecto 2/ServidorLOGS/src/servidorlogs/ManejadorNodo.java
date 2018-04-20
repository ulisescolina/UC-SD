/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorlogs;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class ManejadorNodo extends Thread{
    
    private Socket s;
    private String nombre;
    
    public ManejadorNodo (Socket s, String nombre)
    {
        this.s = s;
        this.nombre = "nodo."+nombre+".log";
        
    }
    
    public void run()
    {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String buscar = dis.readUTF();
            this.buscar(buscar);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorNodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ejemplo grep 
    // pasando string a buscar como argumento y 
    // teniendo asociado el archivo en el atributo archivo
    // se guarda stdout en salida y luego se imprime el resultado
    // ustedes deben enviar el resultado al cliente
    public void buscar(String buscar) {
        try {
            String cadena;
            // comando a ejecutar
            ProcessBuilder pb  = new ProcessBuilder("grep", buscar, this.nombre);
            // inicia el proceso
            Process p = pb.start();
            // obtiene stdout y lo guarda en salida
            BufferedReader salida = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // coleccion que vamos a pasar a través del socket como objeto serializado
            ArrayList<String> resultados = new ArrayList<>();
            // imprime los valores obtenidos
            while ((cadena = salida.readLine()) != null) {
               resultados.add(cadena);
            }
            
            // Creamos un flujo de objetos con el que vamos a pasar el Arraylist
            ObjectOutputStream oos = new ObjectOutputStream(this.s.getOutputStream());
            // Mandamos los datos
            oos.writeObject(resultados);
            
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }
}

