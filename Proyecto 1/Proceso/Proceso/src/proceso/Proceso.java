/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author netbeans
 */
public class Proceso {

    /**
     * @param args the command line arguments
     */
    private static String[][] hoststxt = {{"localhost", "7001"},{"localhost", "7002"},{"localhost", "7003"}};
    private static String[] server = {"localhost", "7000"};
    
    public static void main(String[] args) {
        try {
            int port = 7002;
            Listener l = new Listener();
            l.start();
            
            Socket s = new Socket("localhost", 7000);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF("UP"+ String.valueOf(port));
            
            //ServerSocket ss = new ServerSocket(Integer.valueOf(args[0]));
            ServerSocket ss = new ServerSocket(port);
            Socket sEscucha = ss.accept();
            DataInputStream dis = new DataInputStream(sEscucha.getInputStream());
            String sign = dis.readUTF();
            System.out.println(sign);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
