/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasificador;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.comun.LibComun;

/**
 *
 * @author netbeans
 */
public class Server {
    
    HashMap<String, String[]> hosts = new HashMap<>();
    private static String[] server = {"localhost", "7000"};
    
    int clientesConectados = 0;
    public void iniciar()
    {
        Iterator<Map.Entry<String, String[]>> it;
        LibComun lc = new LibComun(); // instanciamos libreria que contiene metodos comunes entre proyectos, ej: obtener las lineas de hosts:puerto.
        this.hosts = lc.getHosts("hosts.txt"); // obtenemos los host:puerto del archivo hosts.txt
        try {
            // Socket servidor que espera la conexion de los procesos
            ServerSocket svc = new ServerSocket(Integer.parseInt(server[1]));
            while (clientesConectados < hosts.size()) { // Con esto conocemos cuantas lineas tiene el archivo hosts, por ende la cantidad de clientes que esperamos.
                                                        // tendriamos problemas si uno de ellos no se conecta. Como se dijo en clase, se supone que los clientes fallan
                // esperamos a que los procesos se reporten como "levantados"
                Socket req = svc.accept();
                DataInputStream dis = new DataInputStream(req.getInputStream());
                String ready = dis.readUTF();
                System.out.println(ready);
                /*Agregar tratamiento para la recepcion de mensaje*/
                clientesConectados++;
            }
            
            /*
                A partir de acá los procesos esperan a que se les de la señal de
                inicio para empezar a trabajar.
            */
            // Obtengo el iterador de la coleccion hosts
            it = lc.getIterator(hosts);
            while (it.hasNext()) {  
                Map.Entry<String, String[]> elemento = it.next();
                /* Con elemento estoy accediendo a uno de los objetos del iterador
                getKey: devuelve la clave del elemento.
                getValue: devuelve el valor del elemento. */
                Socket s = new Socket(elemento.getValue()[0], Integer.parseInt(elemento.getValue()[1]));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                // enviamos la señal de inicio.
                dos.writeUTF("START");
            }
            
        } catch (IOException ex) {
                Logger.getLogger(Clasificador.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}
