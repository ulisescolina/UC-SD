package Servidores;

import org.apache.thrift.TException;
import uranob.Bloques;

public class ManejadorMB implements ServMB.Iface {
    
    private Bloques servB;
    
    public ManejadorMB(Bloques b){
        this.servB = b;
    }
    
    @Override
    public boolean existeBloque(String valorHash) throws TException {
        return this.servB.getDbBloques().containsKey(valorHash);
    }
    
}
