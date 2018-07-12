/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3;

/**
 *
 * @author urc
 */
public class P3 {
    
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) {
            System.err.println("La cantidad de argumentos es incorrecta.\nSe aceptan:\n\t- 2 (dos) argumentos para un proceso introductor y,\n\t- 4 (cuatro) argumentos para un proceso simple");
        } else if (args.length == 2) {
            // Crear un introductor
            Proceso i = new Proceso(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            Escuchador ei = new Escuchador(i);
            ei.start();
//            Fallador fi = new Fallador(i);
//            fi.start();
            MarcaPaso mpi = new MarcaPaso(i);
            mpi.start();
        } else if (args.length == 4) {
            // Crear un simple
            Proceso s = new Proceso(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            s.solicitarAcceso();
            Escuchador es = new Escuchador(s);
            es.start();
//            Fallador fs = new Fallador(s);
//            fs.start();
            MarcaPaso mps = new MarcaPaso(s);
            mps.start();
        }
    }
    
}
