/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasificador;

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
public class Server {
    
    HashMap<String, String[]> hosts = new HashMap<>();
    private static final String[][] server = {{"localhost", "7000"},{"localhost", "6999"}};
    int clientesConectados = 0;
    public void iniciar()
    {
        Iterator<Map.Entry<String, String[]>> it;
        LibComun lc = new LibComun(); // instanciamos libreria que contiene metodos comunes entre proyectos, ej: obtener las lineas de hosts:puerto.
        this.hosts = lc.getHosts("hosts.txt"); // obtenemos los host:puerto del archivo hosts.txt
        try {
            // Socket servidor que espera la conexion de los procesos
            ServerSocket svc = new ServerSocket(Integer.parseInt(server[0][1]));
            while (clientesConectados < hosts.size()) { // Con esto conocemos cuantas lineas tiene el archivo hosts, por ende la cantidad de clientes que esperamos.
                                                        // tendriamos problemas si uno de ellos no se conecta. Como se dijo en clase, se supone que los clientes no fallan
                // esperamos a que los procesos se reporten como "levantados"
                Socket req = svc.accept();
                DataInputStream dis = new DataInputStream(req.getInputStream());
                String ready = dis.readUTF();
                System.out.println(ready);
                /*Agregar tratamiento para la recepcion de mensaje*/
                clientesConectados++;
                // Cerramos la conexion del socket
                dis.close();
                req.close();
            }
            svc.close();
            
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
                // Cerramos la conexion del socket
                dos.close();
                s.close();
            }
            
            /*
                A partir de aca inicia el procedimiento para emitir el informe
            */
            svc = new ServerSocket(Integer.parseInt(server[1][1]));
            int datoProceso=0;
            this.imprimirCabecera();
            ArrayList<String[]> datosSeparadosProcesos = new ArrayList<>();
            while (datoProceso < hosts.size()) { // Con esto conocemos cuantas lineas tiene el archivo hosts, por ende la cantidad de clientes que esperamos.
                                                // tendriamos problemas si uno de ellos no se conecta. Como se dijo en clase, se supone que los clientes no fallan
                // esperamos a que los procesos reporten sus datos
                Socket req = svc.accept();
                DataInputStream dis = new DataInputStream(req.getInputStream());
                String datos = dis.readUTF();
                //Cargamos, en una coleccion, los arrays que contienen los campos que representan los datos del proceso Client
                datosSeparadosProcesos.add(this.separarArray(datos));
                datoProceso++;
                // Cerramos los sokets utilizados para recibir los datos
                dis.close();
                req.close();
            }
            svc.close();
            this.realizarInforme(datosSeparadosProcesos);
        } catch (IOException ex) {
                Logger.getLogger(Clasificador.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void realizarInforme(ArrayList<String[]> dp)
    {
        Iterator<String[]> it = dp.iterator();
        int auxSumaMsjRecibidos=0, auxSumaMsjEnviados = 0, auxControlMsjRecibidos=0, auxControlMsjEnviados=0;
        while (it.hasNext()) {
            String[] datosProceso = it.next();
            auxControlMsjEnviados+=Integer.parseInt(datosProceso[2]);
            auxControlMsjRecibidos+=Integer.parseInt(datosProceso[1]);
            auxSumaMsjEnviados+=Integer.parseInt(datosProceso[3]);
            auxSumaMsjRecibidos+=Integer.parseInt(datosProceso[4]);
            this.armarDatos(datosProceso[0], Integer.parseInt(datosProceso[2]), Integer.parseInt(datosProceso[1]), Integer.parseInt(datosProceso[3]), Integer.parseInt(datosProceso[4]));
        }
        System.out.println("-----------------------------------------------------------------------------------");
        this.armarDatos("\tTOTAL", auxControlMsjEnviados, auxControlMsjRecibidos, auxSumaMsjEnviados, auxSumaMsjRecibidos);
    }
    
    public void imprimirCabecera()
    {
        System.out.println("Esperamos a que lleguen los datos de cada proceso.");
        System.out.println("La cantidad de hosts es: "+this.hosts.size());
        System.out.println("CME: Cant. Msj. Enviados\nCMR: Cant. Msj. Recibidos\nSME: Suma Msj. Enviados\nSMR: Suma Msj. Recibidos");
        System.out.println("\tNODO\t|\tCME\t|\tCMR\t|\tSME\t|\tSMR");
        System.out.println("-----------------------------------------------------------------------------------");
    }
    
    public String[] separarArray(String datosProceso)
    {
        String[] datosProcesoSeparados = {""};
        if (datosProceso.contains(";")) {
            datosProcesoSeparados = datosProceso.split(";");
            if (datosProcesoSeparados.length == 5) {
                return datosProcesoSeparados;
            } else {
                throw new IllegalArgumentException("La cantidad de datos en \""+datosProcesoSeparados+"\"="+datosProcesoSeparados.length+" es superior a la esperada");
            }
        } else {
            throw new IllegalArgumentException("La los datos para el mensaje \"" + datosProcesoSeparados + "\" no contiene el separador \"|\"");
        }
    }
    
    private void armarDatos(String host, int msjEnviados, int msjRecepciones, int sumaEnvios, int sumaRecepciones)
    {
        System.out.println(host+"\t|\t"+String.valueOf(msjEnviados)+"\t|\t"+String.valueOf(msjRecepciones)+"\t|"+String.valueOf(sumaEnvios)+"\t|\t"+String.valueOf(sumaRecepciones));
    }
}
