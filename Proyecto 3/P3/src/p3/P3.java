/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author urc
 */
public class P3 {
    private static Proceso p;
    private final static Logger LOG = Logger.getLogger("inicio");
    public static void main(String[] args) {
        boolean seguir = true;
        if (args.length != 2 && args.length != 4) {
            System.err.println("La cantidad de argumentos es incorrecta.\nSe aceptan:\n\t- 2 (dos) argumentos para un proceso introductor y,\n\t- 4 (cuatro) argumentos para un proceso simple");
            LOG.log(Level.SEVERE, "La cantidad de parametros proveidos para iniciar el programa es incorrecto."); // esto no va a registrar nada en el log porque no tiene un handler para un archivo
            return;
        } else if (args.length == 2) {
            P3.configurarLog(Integer.parseInt(args[0]));
            try {
                // Crear un introductor
                Proceso i = new Proceso(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                Escuchador ei = new Escuchador(i);
                ei.start();
                MarcaPaso mpi = new MarcaPaso(i);
                mpi.start();
                Fallador fi = new Fallador(i);
                fi.start();
                p = i;
            } catch (Exception e) {
                P3.getLog().log(Level.SEVERE, "Error al obtener host");
            }
        } else if (args.length == 4) {
            P3.configurarLog(Integer.parseInt(args[0]));
            try {
                // Crear un simple
                Proceso s = new Proceso(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
                s.solicitarAcceso();
                Escuchador es = new Escuchador(s);
                es.start();
                MarcaPaso mps = new MarcaPaso(s);
                mps.start();
                Fallador fs = new Fallador(s);
                fs.start();
                p = s;
            } catch (Exception e) {
                P3.getLog().log(Level.SEVERE, "Error al obtener host");
            }
        }

        
        Menu m = new Menu(p);
        Menu.limpiarPantalla();
        while (seguir) {
            m.imprimir();
        }
    }
    
    private static void configurarLog(int id) {
	// id es el ID del nodo
        try {
            Handler fileHandler = new FileHandler("./log."+id+".log", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            Handler consoleHandler = new ConsoleHandler();
            fileHandler.setLevel(Level.ALL);
            consoleHandler.setLevel(Level.WARNING);
                        
            P3.getLog().setUseParentHandlers(false);
            P3.getLog().addHandler(fileHandler);
            P3.getLog().addHandler(consoleHandler);

        } catch (IOException | SecurityException ex) {
            P3.getLog().log(Level.SEVERE, "Error en LOGGER");
        }
    }
    
    public synchronized static final Logger getLog(){
        return LOG;
    }
}
