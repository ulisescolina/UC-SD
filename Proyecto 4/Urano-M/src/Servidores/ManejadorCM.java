package Servidores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ManejadorCM implements ServCM.Iface{

    private HashMap<String, ArrayList<String>> dbMetadatos = new HashMap<>();
    private final String SEPARADOR="::";
    
    /**
     * Recibe del cliente el <i>nombre</i> del archivo, con sus <i>listaHash</i>, y la <i>marcaTiempo</i>.
     * Conecta con el servidor de bloques y comprueba que los hash's estén cargados.
     * Si falta algún hash devuelve un List con los mismos.
     * Si todos los hash's están cargados, carga la <i>listaHash</i> en memoria asociandola con el nombre correspondiente.
     * @param nombre Nombre del archivo a guardar
     * @param listaHash Lista de los hash's de cada una de las partes del archivo, en orden
     * @param marcaTiempo Timestamp del cliente, indica la "version" del archivo
     * @return Lista de hash's faltantes en el servidor de bloques, o en su defecto List vacío
     * @throws TException 
     */
    @Override
    public List<String> almacenarArchivo(String nombre, List<String> listaHash, long marcaTiempo) throws TException {
        // Defino la conexion con el servidor de bloques
        TTransport transporte = new TSocket("localhost", 15000);
        TProtocol protocolo = new TBinaryProtocol(transporte);
        ServMB.Client bloques = new ServMB.Client(protocolo);
        // Abro la conexión (opcional)
        transporte.open();
        // Pregunta al servidor de bloques si existen los bloques
        // Guarda en la tabla de metadatos
        System.out.println("dbMeta inicial: ");
        System.out.println(dbMetadatos);
        List<String> bloquesFaltantes = new ArrayList<>();
        Iterator<String> it = listaHash.iterator();
        // Busco cada hash en Bloques, los que faltan agrego en 'bloquesFaltantes'
        while (it.hasNext()) {
            String hash = it.next();
            if (!bloques.existeBloque(hash)){
                bloquesFaltantes.add(hash);
            }
        }
        transporte.close();
        // Si todos los bloques están cargados en Bloques 
        //  elimino cualquier asociación de archivo viejo
        //  y agrego el nuevo
        if (bloquesFaltantes.isEmpty()){
            String clave = this.buscarArchivoEnDB(nombre);
            if (clave != null) {
                this.eliminarArchivo(clave);
                System.out.println("Asociación vieja eliminada. Clave: " + clave);
            }
            this.addDbMetadatos(nombre + this.SEPARADOR + marcaTiempo, (ArrayList<String>) listaHash);
        }
        System.out.println("---");
        //System.out.println(bloquesFaltantes);
        System.out.println("dbMeta final: ");
        System.out.println(dbMetadatos);
        System.out.println("//////////////////////////////////////////");
        return bloquesFaltantes;
    }

    @Override
    public List<String> obtenerArchivo(String nombre) throws TException {
        Iterator<Entry<String, ArrayList<String>>> it = this.dbMetadatos.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, ArrayList<String>> e = it.next();
            String clave = e.getKey();
            // Obtenemos la primer parte de la clave
            // La clave tiene el formato de "nombre:marca_tiempo"
            String name = clave.split(this.SEPARADOR)[0]; 
            if (name.equals(nombre)){
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * Elimina la asociación del archivo con los hashs. Los bloques se mantienen
     * @param nombre Clave en el HashMap a borrar: "archivo:marca_tiempo"
     * @return true si se eliminó, false si se produjo un error al intentar eliminar
     * @throws TException Mensaje de error en pantalla
     */
    @Override
    public boolean eliminarArchivo(String nombre) throws TException {
        try {
            Iterator<Entry<String, ArrayList<String>>> it = this.dbMetadatos.entrySet().iterator();
            nombre = this.buscarArchivoEnDB(nombre);
            if (nombre != null) {
                this.dbMetadatos.remove(nombre);
                return true;
            }else{
                throw new TException("Intentando eliminar " + nombre + ". El archivo no se encontró");
            }
            /*while(it.hasNext()) {
                Entry<String, ArrayList<String>> e = it.next();
                String clave = e.getKey();
                // Obtenemos la primer parte de la clave
                // La clave tiene el formato de "nombre:marca_tiempo"
                if (clave.equals(nombre)){
                    this.dbMetadatos.remove(clave);
                    break;
                }
            }*/
        } catch (TException e) {
            System.err.println(e);
            return false;
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
    
    /**
     * Busca <b>nombre</b> en la <b>dbMetadatos</b> sin tener en cuenta ":marca_tiempo"
     * 
     * @param nombre Nombre (path+nombre) del archivo que se busca
     * @return La clave completa: "nombre_archivo:marca_tiempo"
     */
    @Override
    public String buscarArchivoEnDB(String nombre){
        Iterator<Entry<String, ArrayList<String>>> it = this.dbMetadatos.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, ArrayList<String>> e = it.next();
            String clave = e.getKey();
            String name = clave.split(this.SEPARADOR)[0]; 
            if (name.equals(nombre)){
                return e.getKey();
            }
        }
        return null;
    }
}
