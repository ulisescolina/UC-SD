/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Proceso {

    
    
    public static void main(String[] args) throws UnknownHostException {
        if (args.length > 1) {
            System.err.println("Solo se espera un argumento que represente al Puerto.");
            return;
        } else if (args.length < 1) {
            System.err.println("Se necesita indicar el puerto a conectarse.");
            return;
        }
        
        Client c = new Client(Integer.parseInt(args[0]));
        c.conectar();
    }
    
}
