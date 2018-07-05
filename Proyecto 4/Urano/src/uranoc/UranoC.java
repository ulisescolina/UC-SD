/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uranoc;

/**
 *
 * @author urc
 */
public class UranoC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        if (args.length == 4){
            switch (args[2].toLowerCase()){
                
                case "subir":
                    // Caso para subir archivo
                    
                case "borrar":
                    // Borramos el archivo
                    
                case "bajar":
                    // Descargamos el archivo
                    
                default:
                    System.err.println("Parámetro desconocido.");
            }
        }else{
            System.err.println("Cantidad de parámetros incorrecta");
        }
    }
    
}
