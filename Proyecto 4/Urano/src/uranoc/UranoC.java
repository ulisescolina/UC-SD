/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uranoc;

import java.io.IOException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author urc
 */
public class UranoC {

    /**
     * @param args [0] = archivo de configuración; [1] = directorio base
     * [2] = comando; [3] = nombre de archivo
     * @throws org.apache.thrift.TException
     * @throws org.apache.thrift.transport.TTransportException
     * @throws java.io.IOException Al intentar crear el archivo al momento de descargarlo
     */
    public static void main(String[] args) throws TException, TTransportException, IOException {
        Cliente cliente = new Cliente();
        if (args.length == 4){
            switch (args[2].toLowerCase()){
                
                case "subir":
                    // Caso para subir archivo
                    cliente.subir(args[1], args[3]);
                    break;
                    
                case "borrar":
                    // Borramos el archivo
                    // No me interesa el PATH, modificar tratamiento de parámetros
                    cliente.borrar(args[3]);
                    break;
                    
                case "bajar":
                    // Descargamos el archivo. PATH en el cual descargamos
                    // y nombre del archivo que descargamos
                    cliente.bajar(args[1], args[3]);
                    break;
                    
                default:
                    System.err.println("Parámetro desconocido.");
            }
        }else{
            System.err.println("Cantidad de parámetros incorrecta");
        }
    }
    
}
