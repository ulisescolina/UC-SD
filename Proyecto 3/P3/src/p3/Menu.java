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
public class Menu {
    
    private Proceso p;
    private Mensajes m;
    public Menu(Proceso p) {
        this.p = p;
        this.m = new Mensajes();
    }
    
    public void imprimir() {
        
        System.out.println(Mensajes.MENU);
    }
    
    public void imprimirAyuda(){
        System.out.println(m.ayuda());
    }
    
    public static void limpiarPantalla() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
    
}
