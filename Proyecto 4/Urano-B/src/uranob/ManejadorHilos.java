package uranob;

import org.apache.thrift.server.TServer;


public class ManejadorHilos extends Thread {
    
    TServer servidor;
    
    public ManejadorHilos (TServer servidor){
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        this.servidor.serve();
    }
    
}
