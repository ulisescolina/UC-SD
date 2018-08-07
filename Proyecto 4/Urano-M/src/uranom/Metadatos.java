package uranom;

import java.util.ArrayList;
import java.util.HashMap;
import Servidores.*;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;

public class Metadatos {
    
    public void iniciar() {
        try {
            // creamos una instancia del manejador
            ManejadorCM manejador = new ManejadorCM();
            // inicializamos un procesador para el manejador
            ServCM.Processor procesador = new ServCM.Processor(manejador);
            // usamos sockets TCP (El servidor escucha en el puerto 15001)
            // Los siguientes servidores de metadatos escuchan en los puertos subsiguientes
            TServerTransport servidorTransporte = new TServerSocket(15001);
                       
            // defino argumentos a pasar al servidor (TThreadPoolServer)
            TThreadPoolServer.Args argumentos = new TThreadPoolServer.Args(servidorTransporte);
            
            argumentos.processor(procesador);
            argumentos.transportFactory(new TTransportFactory());
            argumentos.protocolFactory(new TBinaryProtocol.Factory());            

            // al definir un TThreadPoolServer puedo pasarle de manera opcional la cantidad mínima y máxima de hilos a usar
//            argumentos.minWorkerThreads(10);
//            argumentos.maxWorkerThreads(50);

            // defino formato del servidor
            TServer servidor = new TThreadPoolServer(argumentos);

            System.out.println("Iniciando servidor de metadatos...");
            // inicio el servidor
            servidor.serve();
        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor.");
        }
    }   
}
