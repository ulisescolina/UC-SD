/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author netbeans
 */

public class ManejadorListener extends Thread{
    
    Socket comunicacionProcesos;
    Client procesoCreador;
    Listener l;
    
    public ManejadorListener(Socket socket, Listener l) {
        this.comunicacionProcesos = socket;
        this.procesoCreador = l.getC();
        this.l = l;
    }
    
    @Override
    public void run() {
        try {
            // obtenemos el data input stream del socket que nos pasa el proceso listener.
            DataInputStream dis = new DataInputStream(comunicacionProcesos.getInputStream());
            int cmr = this.procesoCreador.getMensajesPorRonda();
            OUTER:
            for (int i = 0; i < cmr; i++) {
                String entero = dis.readUTF();
                switch (entero) {
                    case "FIN":
                        // preguntamos si es la señal que manda uno de los Senders que indica que este finalizo
                        l.incProcesosTerminados(); // Aumentamos el contador que registra la cantidad de Senders que finalizaron
                        if (l.getProcesosTerminados() == l.getC().getHosts().size()) { // Aca pregntamos por el ultimo hilo que se crea, para que este pueda avisarle al Listener que ya puede dejar de escuchar
                            Socket s = new Socket("localhost", comunicacionProcesos.getLocalPort()); // establecemos la conexion tcp con el Listener que es encargado de crear los hilos
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            dos.writeUTF("KILL"); // Mensaje que logra que el Listener salga del bucle y deje de escuchar
                        }    System.out.println("Proceso finalizado "+l.getProcesosTerminados());
                        break OUTER;
                    case "KILL":
                        System.out.println("Fin");
                        break OUTER;
                    default:
                        this.procesoCreador.incrementarSumaRecepciones(Integer.parseInt(entero));
                        this.procesoCreador.incrementarControlRecepciones();
                        break;
                }
            }
            dis.close();
            this.comunicacionProcesos.close();
        } catch (IOException ex) {
            Logger.getLogger(ManejadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
