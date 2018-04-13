/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasificador;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Clasificador {
    
    private int nroRondas, mjsEnviadosXRonda;

    // Atributos temporales serán reemplazados por archivo hosts.txt
    
    ArrayList<Integer> puerto = new ArrayList<>();
    private static String[][] hoststxt = {{"localhost", "7001"},{"localhost", "7002"},{"localhost", "7003"}};
    private static String[] server = {"localhost", "7000"};
    
    public static void main(String[] args) {
        
    
        int clientesConectados = 0;
        try {
            ServerSocket svc = new ServerSocket(Integer.parseInt(server[1]));
            while (clientesConectados < 2) {
                Socket req = svc.accept();;
                DataInputStream dis = new DataInputStream(req.getInputStream());
                String ready = dis.readUTF();
                System.out.println(ready);
                /*Agregar tratamiento para la recepcion de mensaje*/
                clientesConectados++;
            }
            
            for (int i=0; i < 2; i++) {
                Socket s = new Socket("localhost", Integer.parseInt(hoststxt[i][1]));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF("START");
            }
        } catch (IOException ex) {
            Logger.getLogger(Clasificador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
