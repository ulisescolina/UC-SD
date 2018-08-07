package uranoc;

import Servidores.ServCB;
import Servidores.ServCM;
import Servidores.ServMB;
//import com.sun.corba.se.impl.ior.ByteBuffer;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Cliente {

    private static final int TAMANIO_BLOQUE = 4194304;
    private TTransport transporteMetadatos, transporteBloques;

    /**
     * Obtiene un archivo, y lo separa en partes de igual tamaño
     *
     * @param archivo Ruta y nombre del archivo a particionar
     * @return HashMap con (SHA-256, bloque del archivo)
     */
    public LinkedHashMap<String, byte[]> particionador(String archivo) {
        File ifile = new File(archivo);
        //HashMap<String,byte[]> listaBloques = new HashMap<>();
        LinkedHashMap<String, byte[]> listaBloques = new LinkedHashMap<>();
        if (ifile.exists()) {
            FileInputStream fis;
            int fileSize = (int) ifile.length();
            int read = 0, readLength = Cliente.TAMANIO_BLOQUE;
            String parte = "";
            int cont = 0;
            byte[] byteChunk;
            try {
                fis = new FileInputStream(ifile);
                // Mientras no haya llegado al final del archivo
                while (fileSize > 0) {
                    cont++;
                    // Ultimo bloque del archivo
                    if (fileSize <= Cliente.TAMANIO_BLOQUE) {
                        readLength = fileSize;
                    }
                    byteChunk = new byte[readLength];
                    read = fis.read(byteChunk, 0, readLength); // Leo el tamanio definido
                    listaBloques.put(Cliente.hasheador(byteChunk, fileSize), byteChunk);
                    //parte = String.valueOf(cont);
                    //listaBloques.put(parte,byteChunk);
                    fileSize -= read;
                    byteChunk = null;
                }
                fis.close();
            } catch (Exception e) {
                System.err.println("Error al particionar el archivo");
            }

        } else {
            System.err.println("Archivo inexistente, o path incorrecto");
        }
        return listaBloques;
    }

    /**
     * Obtiene las distintas partes de un archivo y las une en uno solo
     *
     * @param listaBloques Corresponde a los hash + partes del archivo
     * @return byte[] Son todas las partes unidas
     */
    public byte[] juntador(LinkedHashMap<String, byte[]> listaBloques) {
        // Corregir el tamanio del archivo final.
        // Habría que sumar el lenght de cada elemento del Hash para obtener el tamanio final del archivo
        Iterator<Entry<String, byte[]>> it = listaBloques.entrySet().iterator();
        int tamanio = 0;
        while (it.hasNext()) {
            tamanio += it.next().getValue().length;
        }
        //byte[] compilado = new byte[TAMANIO_BLOQUE * listaBloques.size()];
        byte[] compilado = new byte[tamanio];
        it = listaBloques.entrySet().iterator();
        int indice = 0;
        while (it.hasNext()) {
            Entry<String, byte[]> e = it.next();
            //byte[] a = new byte[e.getValue().length];
            //byte[] a = e.getValue();
            System.arraycopy(e.getValue(), 0, compilado, indice, e.getValue().length);
            indice = indice + e.getValue().length;
            //compilado = ArrayUtils.addAll(compilado, e.getValue());
        }
        return compilado;
    }

    public static String hasheador(byte[] bloque, int i) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(bloque);
            String sha256hex = DigestUtils.sha256Hex(encodedhash);
            //System.out.println("hash: " + sha256hex);
            return sha256hex;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String obtenerNombre(String archivo) {
        String[] path = archivo.split("\\" + File.separator);
        return path[path.length - 1];
    }

    public Boolean subir(String path, String archivo) throws TTransportException, TException {
        // hashesBloques = archivo particionado con sus hashes
        LinkedHashMap<String, byte[]> hashesBloques = this.particionador(path + archivo);
        ArrayList<String> hashes = new ArrayList<>();
        hashesBloques.entrySet().stream().forEach((item) -> {
            hashes.add(item.getKey());
        });
        try {
            // Conecto con el servidor de metadatos
            ServCM.Client metadatos = this.conectar2Metadatos();
            this.transporteMetadatos.open();
            List<String> bloquesFaltantes;
            bloquesFaltantes = metadatos.almacenarArchivo(archivo, hashes, System.currentTimeMillis());
            if (!bloquesFaltantes.isEmpty()) {
                // Conecto con Servidor de bloques
                ServCB.Client bloques = this.conectar2Bloques();
                this.transporteBloques.open();
                Iterator it = bloquesFaltantes.iterator();
                while (it.hasNext()) {
                    Object ob = it.next();
                    String hash = ob.toString();
                    byte[] bytes = hashesBloques.get(hash);
                    ByteBuffer buf = ByteBuffer.wrap(bytes);
                    bloques.almacenarBloque(hash, buf);
                }
                this.transporteBloques.close();
                // Vuelvo a enviar a MD para que lo almacene
                metadatos.almacenarArchivo(archivo, hashes, System.currentTimeMillis());
                this.transporteMetadatos.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public Boolean borrar(String archivo) throws TTransportException, TException{
        ServCM.Client metadatos = this.conectar2Metadatos();
        this.transporteMetadatos.open();
        if (metadatos.eliminarArchivo(archivo)){
            return true;
        }else{
            System.err.println("No se encuentra el nombre del archivo");
            return false;
        }
    }
    
    public void bajar(String path, String archivo) throws TTransportException, TException, IOException{
        // Solicito a MD que me pase los hash
        ServCM.Client metadatos = this.conectar2Metadatos();
        this.transporteMetadatos.open();
        List<String> hashes = metadatos.obtenerArchivo(archivo);
        this.transporteMetadatos.close();
        ServCB.Client bloques = this.conectar2Bloques();
        this.transporteBloques.open();
        Iterator it = hashes.iterator();
        LinkedHashMap<String, byte[]> listaBloques = new LinkedHashMap<>();
        String hash;
        byte[] datos;
        while(it.hasNext()){
            hash = it.next().toString();
            ByteBuffer buf = bloques.obtenerBloque(hash);
            datos = new byte[buf.remaining()];
            buf.get(datos);
            listaBloques.put(hash, datos);
        }
        this.transporteBloques.close();
        datos = this.juntador(listaBloques);
        try (FileOutputStream fos = new FileOutputStream(path + archivo)) {
            fos.write(datos);
            System.out.println(archivo + " fue descargado satisfactoriamente en: " + path);
        }
    }

    public String directorioActual() {
        File miDir = new File(".");
        try {
            return miDir.getCanonicalPath() + File.separatorChar;
        } catch (Exception e) {
            System.out.println("Error obteniendo directorio actual");
            return null;
        }
    }

    private ServCM.Client conectar2Metadatos() {
        this.transporteMetadatos = new TSocket("localhost", 15001);
        TProtocol protocolo = new TBinaryProtocol(this.transporteMetadatos);
        ServCM.Client metadatos = new ServCM.Client(protocolo);
        return metadatos;
    }

    private ServCB.Client conectar2Bloques() {
        this.transporteBloques = new TSocket("localhost", 14999);
        TProtocol protocoloBloques = new TBinaryProtocol(this.transporteBloques);
        ServCB.Client bloques = new ServCB.Client(protocoloBloques);
        return bloques;
    }
    
    /**
     * Separa el nombre del archivo del resto del PATH, y lo devuelve.
     * Por ejemplo: recibe "c:\carpeta\archivo.txt" y devuelve "archivo.txt"
     * @param archivo PATH completo, sea de windows o unix
     * @return Todo hacia la derecha del último separador "/" o "\"
     */
    private String separarPath(String archivo) {
        String[] partes = archivo.split(Pattern.quote(File.separator));
        return partes[partes.length-1];        
    }
    
}
