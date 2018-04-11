/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto01;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 *
 * @author seven
 */
public class Proyecto01 {

    private int nroRondas, mjsEnviadosXRonda;

    // Atributos temporales serán reemplazados por archivo hosts.txt
    private static String[][] hoststxt = {{"localhost", "7001"},{"localhost", "7002"},{"localhost", "7003"}};
    private static String[] server = {"localhost", "7000"};

    /**
     * @param args the command line arguments
     */
    public void main(String[] args) throws IOException {
        //Creo el hilo nuevo pasandole los datos sobre host:port sobre el cual operar
        Proceso P01 = new Proceso(hoststxt, server, 0, "P1");
        Proceso P02 = new Proceso(hoststxt, server, 1, "P2");
        Proceso P03 = new Proceso(hoststxt, server, 2, "P3");
        P01.start();
        P02.start();
        P03.start();
        
        this.iniciarProcesamiento(P01);
/*
        try {
                Socket req = svc.accept();
                BufferedReader delCliente = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String linea = delCliente.readLine();
                DataOutputStream alCliente = new DataOutputStream(req.getOutputStream());
                alCliente.writeBytes(linea.toUpperCase() + '\n');

                try {
                    alCliente.close();
                    delCliente.close();
                    req.close(); // cerrar socket de conexión
                    svc.close(); // cerrar ServerSocket
                } catch (IOException e) {
                    System.out.println("Error al desconectarse");
                }

            } catch (IOException ex) {
                System.out.println("Error al esperar por conexiones");
            }*/
    }
    
    public void iniciarProcesamiento(Proceso px) throws IOException{
        //Ahora debería enviar las señales de inicio
        ServerSocket svc = new ServerSocket(Integer.parseInt(server[1]));
        //Socket req = svc.accept();
        SocketAddress skadd = new SocketAddress();
        svc.bind("localhost");
        DataOutputStream alCliente = new DataOutputStream(req.getOutputStream());
        alCliente.writeBytes("START"+'\n');
        // ver .writeUTF y readUTF
    }

}
