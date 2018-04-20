/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorlogs;

import java.util.Scanner;

/**
 *
 * @author netbeans
 */
public class ServidorLOGS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        System.out.print("Ingrese puerto en el cual va a escuchar el servidor: ");
        Scanner puerto = new Scanner(System.in);
        String scPuerto = puerto.nextLine();
        
        System.out.print("Ingrese el id del servidor: ");
        Scanner id = new Scanner(System.in);
        String scId = id.nextLine();
        
        Nodo n = new Nodo(Integer.parseInt(scPuerto), scId);
        n.iniciar();
    }
    
}
