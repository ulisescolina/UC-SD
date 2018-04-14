/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasificador;

/**
 *
 * @author netbeans
 */
public class Clasificador {
    
    private int nroRondas, mjsEnviadosXRonda;

    public static void main(String[] args) {
        Server s = new Server();
        s.iniciar();
    }
    
}
