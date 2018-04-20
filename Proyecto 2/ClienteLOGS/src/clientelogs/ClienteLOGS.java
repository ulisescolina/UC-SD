/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientelogs;

import java.util.Scanner;
import lib.comun.LibComun;

/**
 *
 * @author netbeans
 */
public class ClienteLOGS {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        LibComun lc = new LibComun();
        //Instanciamos al cliente que se va a encargar de realizar las consultas
        Cliente c = new Cliente(lc.getIterator(lc.getHosts("servers.txt")));
        
        System.out.print("Ingrese el termino que desea buscar en los servidores: ");
        Scanner sc = new Scanner(System.in);
        String consulta = sc.nextLine();
        
        // iniciamos la consulta
        c.consultar(consulta);
    }
}
