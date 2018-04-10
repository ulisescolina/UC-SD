/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto01;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.DatagramSocket;

/**
 *
 * @author seven
 */
public class Proyecto01 {

    private int nroRondas, mjsEnviadosXRonda;
    
    // Atributos temporales serán reemplazados por un archivo
    private static final String[] host01 = {"localhost","7001"};
    private static final String[] host02 = {"localhost","7002"};
    private static final String[] host03 = {"localhost","7003"};
    private static final String[] host04 = {"localhost","7004"};
    private static final String[] host05 = {"localhost","7005"};
    private static final String[] server = {"localhost","6001"};
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Creo el hilo nuevo pasandole los datos sobre host:port sobre el cual operar
        Proceso neurona01 = new Proceso(host01, server);
        neurona01.start();
        ServerSocket svc = new ServerSocket(Integer.parseInt(server[1]));
        Socket neurona01th = svc.accept(); // Quedo a la espera de la respuesta de conexión TCP
        // Recibir el mensaje TCP para comprobar el funcionamiento
    }
    
}
