package Servidores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.thrift.TException;

public class ManejadorCM implements ServCM.Iface{

    private HashMap<String, ArrayList<String>> dbMetadatos = new HashMap<>();
    
    @Override
    public void almacenarArchivo(String nombre, List<String> listaHash, int marcaTiempo) throws TException {
        // Pregunta al servidor de bloques si existen los bloques
        // Guarda en la tabla de metadatos
        //System.currentTimeMillis();
        this.addDbMetadatos(nombre+":"+marcaTiempo, (ArrayList<String>) listaHash);
    }

    @Override
    public List<String> obtenerArchivo(String nombre) throws TException {
        Iterator<Entry<String, ArrayList<String>>> it = this.dbMetadatos.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, ArrayList<String>> e = it.next();
            String clave = e.getKey();
            // Obtenemos la primer parte de la clave
            // La clave tiene el formato de "nombre:marca_tiempo"
            String name = clave.split(":")[0]; 
            if (name.equals(nombre)){
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public void eliminarArchivo(String nombre) throws TException {
        Iterator<Entry<String, ArrayList<String>>> it = this.dbMetadatos.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, ArrayList<String>> e = it.next();
            String clave = e.getKey();
            // Obtenemos la primer parte de la clave
            // La clave tiene el formato de "nombre:marca_tiempo"
            String name = clave.split(":")[0]; 
            if (name.equals(nombre)){
                this.dbMetadatos.remove(clave);
                break;
            }
        }
    }

    public HashMap<String, ArrayList<String>> getDbMetadatos() {
        return dbMetadatos;
    }

    public void setDbMetadatos(HashMap<String, ArrayList<String>> dbMetadatos) {
        this.dbMetadatos = dbMetadatos;
    }
    
    public void addDbMetadatos (String sha, ArrayList<String> listaBloques) {
        this.dbMetadatos.put(sha, listaBloques);
    }
}
