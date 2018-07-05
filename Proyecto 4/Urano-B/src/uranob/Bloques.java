package uranob;

import Servidores.ManejadorCB;
import Servidores.ManejadorMB;
import Servidores.ServCB;
import Servidores.ServMB;
import java.util.HashMap;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;


public class Bloques {
    
    private HashMap<String, byte[]> dbBloques = new HashMap<>();
    
    public void iniciar(){
        try {
            // creamos una instancia del manejador
            ManejadorMB manejadorMB = new ManejadorMB(this);
            // inicializamos un procesador para el manejador
            ServMB.Processor procesador = new ServMB.Processor(manejadorMB);
            // usamos sockets TCP (El servidor escucha en el puerto 15000)
            TServerTransport servidorTransporte = new TServerSocket(15000);
            // defino argumentos a pasar al servidor (TThreadPoolServer)
            TThreadPoolServer.Args argumentos = new TThreadPoolServer.Args(servidorTransporte);
            argumentos.processor(procesador);
            argumentos.transportFactory(new TTransportFactory());
            argumentos.protocolFactory(new TBinaryProtocol.Factory());            
            // defino formato del servidor
            TServer servidor = new TThreadPoolServer(argumentos);
            ManejadorHilos servmb = new ManejadorHilos(servidor);
            servmb.start();
            
            // Inicio servidor e hilode CB
            ManejadorCB manejadorCB = new ManejadorCB(this);
            ServCB.Processor procesadorCB = new ServCB.Processor(manejadorCB);
            TServerTransport servidorTransporteCB = new TServerSocket(14999);
            TThreadPoolServer.Args argumentosCB = new TThreadPoolServer.Args(servidorTransporteCB);
            argumentosCB.processor(procesadorCB);
            argumentosCB.transportFactory(new TTransportFactory());
            argumentosCB.protocolFactory(new TBinaryProtocol.Factory());            
            TServer servidorCB = new TThreadPoolServer(argumentosCB);
            ManejadorHilos servCB = new ManejadorHilos(servidorCB);
            servCB.start();
        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor.");
        }
    }

    public HashMap<String, byte[]> getDbBloques() {
        return dbBloques;
    }

    public void setDbBloques(HashMap<String, byte[]> dbBloques) {
        this.dbBloques = dbBloques;
    }
    
    public void addDbBloques(String clave, byte[] bloque) {
        this.dbBloques.put(clave, bloque);
    }
}
