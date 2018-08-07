/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidores;

import java.nio.ByteBuffer;
import org.apache.thrift.TException;
import uranob.Bloques;

public class ManejadorCB implements ServCB.Iface {

    private Bloques servB;

    public ManejadorCB(Bloques b) {
        this.servB = b;
    }

    @Override
    public ByteBuffer obtenerBloque(String valorHash) throws TException {
        byte[] bloque = this.servB.getDbBloques().get(valorHash);
        ByteBuffer buf = ByteBuffer.wrap(bloque);
        return buf;
    }

    @Override
    public boolean almacenarBloque(String valorHash, ByteBuffer bloque) throws TException {
        try {
            byte[] bytes = new byte[bloque.capacity()];
            bloque.get(bytes, 0, bytes.length);
            this.servB.addDbBloques(valorHash, bytes);
            this.verBloquesAlmacenados();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Utilizado solo para depuración.
     * Imprime en pantalla el contenido de la "BD de Bloques"
     */
    public void verBloquesAlmacenados () {
        System.out.println("BDB: ");
        System.out.println(servB.getDbBloques());
    }
    
}
