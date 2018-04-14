/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.comun;


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibComun {

    HashMap<String, String[]> hosts = new HashMap<>();
    
    public HashMap<String, String[]> getHosts(String nombreArchivo) 
    {
        HashMap<String, String[]> auxHosts =  new HashMap<>();
        String archivoHostsTXT = System.getProperty("user.dir") + "/" + nombreArchivo;
        FileReader fr;
        try {
            fr = new FileReader (archivoHostsTXT);
            BufferedReader br = new BufferedReader(fr);
            try {
                String linea;// = br.readLine();
                while((linea=br.readLine())!=null) {
                    if (linea.contains(":")) {
                        String[] hostInfo = linea.split(":");
                        if (hostInfo.length == 2) {
                            auxHosts.put(linea, hostInfo);
                        } else {
                            throw new IllegalArgumentException("La linea \""+linea+"\" tiene una cantidad ilegal de separadores \":\"");
                        }
                    } else {
                        throw new IllegalArgumentException("La linea " + linea + " no contiene el separador de host y puerto \":\"");
                    }
                }
                return auxHosts;
            } catch (IOException ex) {
                Logger.getLogger(LibComun.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                // Cerramos el fichero, para asegurarnos
                // que se cierra tanto si todo va bien como si salta 
                // una excepcion.
                try{                    
                   if( null != fr ){   
                      fr.close();     
                   }                  
                }catch (Exception e2){ 
                   e2.printStackTrace();
                }
             }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LibComun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return auxHosts;
    }

    public Iterator<Map.Entry<String, String[]>> getIterator(HashMap<String, String[]> it)
    {
        return it.entrySet().iterator();
    }

}
