/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

import java.util.Scanner;

/**
 *
 * @author urc
 */
public class P3 {
    private static Proceso p;
    public static void main(String[] args) {
        boolean seguir = true;
        
        if (args.length != 2 && args.length != 4) {
            System.err.println("La cantidad de argumentos es incorrecta.\nSe aceptan:\n\t- 2 (dos) argumentos para un proceso introductor y,\n\t- 4 (cuatro) argumentos para un proceso simple");
        } else if (args.length == 2) {
            // Crear un introductor
            Proceso i = new Proceso(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            Escuchador ei = new Escuchador(i);
            ei.start();
            MarcaPaso mpi = new MarcaPaso(i);
            mpi.start();
            Fallador fi = new Fallador(i);
            fi.start();
            p = i;
        } else if (args.length == 4) {
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
        }
        
        Menu m = new Menu(p);
        Menu.limpiarPantalla();
        while (seguir) {
            m.imprimir();
        }
    }
    
}
