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
public class Mensajes {
    public static final String MENU = ""
            + "===============================================================\n"
            + "\tOpciones\n"
            + "===============================================================\n"
            + "\t\t - Ayuda\n"
            + "\t\t - Vecinos\n"
            + "\t\t - Procesos\n"
            + "===============================================================\n\n";
    
    public String titulo(String s){
        String TITULO = ""
            + "===============================================================\n"
            + "\t"+s+"\n"
            + "===============================================================\n";
        return TITULO;
    }
    
    public String ayuda(){
        String ayuda = titulo("Ayuda");
        ayuda += "- Vecinos: Imprime los procesos vecinos con los que cuenta éste.\n"
            + "- Procesos: Imprime todos los procesos que tiene registrado este en su tabla de procesos.\n"
            + "===============================================================\n\n";
        return ayuda;
    }
}
