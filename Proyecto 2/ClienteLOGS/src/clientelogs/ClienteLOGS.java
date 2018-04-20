/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientelogs;

/**
 *
 * @author netbeans
 */
public class ClienteLOGS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        String consulta = concatenarConsulta(args);
        Cliente c = new Cliente();
        c.consultar(consulta);
    }
    
    /*
        Es utilizado en el main para obtener los parametros pasados al ,jar y
        concatenarlos en un unico String que se va a pasar como consulta
    */
    public static String concatenarConsulta(String[] consulta) {
        String res = "";
        if (consulta.length > 1) {
            for (String c : consulta) {
                res += c;
            }
        } 
        return res;
    }
}
