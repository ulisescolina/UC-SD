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
public class Menu {
    
    private Proceso p;
    private Mensajes m;
    public Menu(Proceso p) {
        this.p = p;
        this.m = new Mensajes();
    }
    
    public void imprimir() {
        Scanner sc = new Scanner(System.in);
        System.out.println(Mensajes.MENU);
        String opcion = "";
            System.out.print("Ingrese una de las opciones: ");
            opcion = sc.next();
            switch(opcion.toLowerCase()){
                case "ayuda":
                    Menu.limpiarPantalla();
                    this.imprimirAyuda();
                    break;
                case "vecinos":
                    Menu.limpiarPantalla();
                    System.out.println(p.imprimirVecinos()+"\n");
                    break;
                case "procesos":
                    Menu.limpiarPantalla();
                    System.out.println(p.imprimirProcesos()+"\n");
                    break;
                case "limpiar":
                    Menu.limpiarPantalla();
                    break;
                default:
                    System.out.println("Esta opcion no esta disponible.\n");
                
            }
    }
    
    public void imprimirAyuda(){
        System.out.println(m.ayuda());
    }
    
    public static void limpiarPantalla() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
    
}
